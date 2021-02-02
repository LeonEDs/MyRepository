package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xad.common.enums.StatusEnum;
import com.xad.server.dto.TagCatalogDto;
import com.xad.server.entity.TagCatalog;
import com.xad.server.entity.TagEntity;
import com.xad.server.mapper.TagCatalogMapper;
import com.xad.server.mapper.TagEntityMapper;
import com.xad.server.service.TagCatalogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签目录 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class TagCatalogServiceImpl implements TagCatalogService
{
    @Autowired
    TagCatalogMapper tagCatalogMapper;

    @Autowired
    TagEntityMapper tagEntityMapper;

    @Override
    public List<TagCatalogDto> queryChildTagCatalogInfo(TagCatalogDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getParentId(), "父节点parentId不能为空");

        TagCatalog queryCondition = new TagCatalog(vo);
        queryCondition.setStatus(StatusEnum.NORMAL.getCode());
        List<TagCatalog> childEntityList = tagCatalogMapper.selectList(new QueryWrapper<>(queryCondition));

        List<TagCatalogDto> result = null;

        if (CollectionUtils.isNotEmpty(childEntityList))
        {
            result = childEntityList.stream().map(TagCatalogDto::new).collect(Collectors.toList());
            result.forEach(dto -> {
                QueryWrapper<TagCatalog> queryExistChild = new QueryWrapper<>();
                queryExistChild.eq("parent_id",  dto.getId());
                queryExistChild.eq("status", StatusEnum.NORMAL.getCode());
                int count = tagCatalogMapper.selectCount(queryExistChild);
                dto.setIsLeaf(count == 0);
            });
        }

        return result;
    }

    @Override
    public List<TagCatalogDto> queryAllTagCatalogInfo()
    {
        QueryWrapper<TagCatalog> queryCondition = new QueryWrapper<>();
        TagCatalog condition = new TagCatalog();
        condition.setStatus(StatusEnum.NORMAL.getCode());
        condition.setParentId(0L);
        condition.setLevel(1);
        queryCondition.setEntity(condition);
        List<TagCatalog> firstLevelList = tagCatalogMapper.selectList(queryCondition); //查询顶层目录

        List<TagCatalogDto> result = null;

        if (CollectionUtils.isNotEmpty(firstLevelList))
        {
            result = firstLevelList.stream().map(TagCatalogDto::new).collect(Collectors.toList()); //转前台交互对象

            result.forEach(first -> {
                condition.setParentId(Long.valueOf(first.getId()));
                condition.setLevel(2);
                queryCondition.setEntity(condition);
                // 查询第二层目录
                List<TagCatalog> secondLevelList = tagCatalogMapper.selectList(queryCondition);
                if (CollectionUtils.isNotEmpty(secondLevelList))
                {
                    first.setIsLeaf(false);
                    List<TagCatalogDto> secondChild = secondLevelList.stream()
                            .map(TagCatalogDto::new).collect(Collectors.toList()); //转前台交互对象
                    first.setChildList(secondChild);

                    secondChild.forEach(second -> {
                        condition.setParentId(Long.valueOf(second.getId()));
                        condition.setLevel(3);
                        queryCondition.setEntity(condition);
                        // 查询第三层目录
                        List<TagCatalog> thirdLevelList = tagCatalogMapper.selectList(queryCondition);
                        if (CollectionUtils.isNotEmpty(thirdLevelList))
                        {
                            second.setIsLeaf(false);
                            List<TagCatalogDto> thirdChild = thirdLevelList.stream()
                                    .map(TagCatalogDto::new).collect(Collectors.toList());
                            second.setChildList(thirdChild);
                        }else
                        {
                            second.setIsLeaf(true);
                        }
                    });

                }else
                {
                    first.setIsLeaf(true);
                }

            });
        }

        return result;
    }

    @Override
    public TagCatalogDto queryTagCatalogInfo(Long id)
    {
        Assert.notNull(id, "ID不能为空");
        TagCatalog condition = new TagCatalog();
        condition.setId(id);
        condition.setStatus(StatusEnum.NORMAL.getCode());

        TagCatalog entity = tagCatalogMapper.selectOne(new QueryWrapper<>(condition));
        return new TagCatalogDto(entity);
    }

    @Override
    public String queryAllTagCatalogName(Long id)
    {
        Assert.notNull(id, "ID不能为空");

        TagCatalog condition = new TagCatalog();
        condition.setId(id);
        condition.setStatus(StatusEnum.NORMAL.getCode());
        String lastName = "";
        String secondName = "";
        String firstName = "";

        TagCatalog entity = tagCatalogMapper.selectOne(new QueryWrapper<>(condition));
        // 非第一层继续查询
        if (entity != null)
        {
            lastName = entity.getCatalogName();
            if (entity.getLevel() != 1)
            {
                condition.setId(entity.getParentId());
                TagCatalog second = tagCatalogMapper.selectOne(new QueryWrapper<>(condition));
                if (second != null)
                {
                    secondName = second.getCatalogName();
                    if (second.getLevel() != 1)
                    {
                        condition.setId(second.getParentId());
                        TagCatalog first = tagCatalogMapper.selectOne(new QueryWrapper<>(condition));
                        // 只查三层
                        if (first != null)
                        {
                            firstName = first.getCatalogName();
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(firstName))
        {
            return firstName + "/" + secondName + "/" + lastName;
        }
        if (StringUtils.isNotEmpty(secondName))
        {
            return secondName + "/" + lastName;
        }
        return lastName;
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public Long saveTagCatalogInfo(TagCatalogDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getParentId(), "parentId不能为空");
        Assert.notNull(StringUtils.isNotEmpty(vo.getCatalogName()), "catalogName不能为空");
        Assert.notNull(StringUtils.isNotEmpty(vo.getCatalogName().trim()), "catalogName不能为空");
        Assert.notNull(vo.getLevel(), "level不能为空");

        QueryWrapper<TagCatalog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catalog_name", vo.getCatalogName().trim());
        queryWrapper.eq("parent_id", vo.getParentId());
        queryWrapper.eq("level", vo.getLevel());
        queryWrapper.eq("status", StatusEnum.NORMAL.getCode());
        TagCatalog dbRecord = tagCatalogMapper.selectOne(queryWrapper);
        Assert.isTrue(dbRecord == null, "标签目录名称已存在");

        TagCatalog entity = new TagCatalog(vo);
        tagCatalogMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public int updateTagCatalogInfo(TagCatalogDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getId(), "Id不能为空");

        TagCatalog entity = new TagCatalog(vo);
        TagCatalog queryCondition = new TagCatalog();
        queryCondition.setStatus(StatusEnum.NORMAL.getCode());
        queryCondition.setId(Long.valueOf(vo.getId()));

        TagCatalog existEntity = tagCatalogMapper.selectOne(new QueryWrapper<>(queryCondition));
        if (existEntity != null)
        {
            return tagCatalogMapper.updateById(entity);
        }
        return 0;
    }

    @Override
    public int deleteTagCatalogInfo(TagCatalogDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getId(), "Id不能为空");

        TagCatalog queryCondition = new TagCatalog();
        queryCondition.setStatus(StatusEnum.NORMAL.getCode());
        queryCondition.setParentId(Long.valueOf(vo.getId()));
        List<TagCatalog> childList = tagCatalogMapper.selectList(new QueryWrapper<>(queryCondition));
        Assert.isTrue(CollectionUtils.isEmpty(childList), "存在子标签目录，请先删除子目录");

        TagEntity tagCondition = new TagEntity();
        tagCondition.setTagCatalogId(Long.valueOf(vo.getId()));
        tagCondition.setStatus(StatusEnum.NORMAL.getCode());
        int count = tagEntityMapper.selectCount(new QueryWrapper<>(tagCondition));
        Assert.isTrue(count <= 0, "该目录下存在标签，请先删除标签");

        vo.setStatus(StatusEnum.DELETED.getCode());
        return updateTagCatalogInfo(vo);
    }
}
