package com.xad;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.xad.utils.valid.field.DomainValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main
{
    private JdbcTemplate jdbcTemplate;

    public Main()
    {
//        Properties properties = new Properties();
//        properties.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
//        properties.setProperty("url", "jdbc:oracle:thin:@192.168.1.189:1521:orcl");
//        properties.setProperty("username", "zfjdgl2");
//        properties.setProperty("password", "zfjdgl2");
//        properties.setProperty("initialSize", "10");
//        properties.setProperty("minIdle", "5");
//        properties.setProperty("maxActive", "20");
//        properties.setProperty("maxWait", "60000");

        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl("jdbc:oracle:thin:@192.168.1.189:1521:orcl");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUsername("zfjdgl2");
        dataSource.setPassword("zfjdgl2");
        try {
            dataSource.init();
        }catch (Exception e)
        {

        }
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) throws Exception
    {
        DataX v_data = new DataX();
        v_data.setCode("AAA");
        String condition = "code=AAA";
        Class clazz = v_data.getClass();

        String c_fieldName = condition.substring(0, condition.indexOf("=")).trim();
        String c_fieldValue = condition.substring(condition.indexOf("=")).trim();
        String c_methodName = "get".concat(c_fieldName.substring(0, 1).toUpperCase().concat(c_fieldName.substring(1)));
        Method c_fieldMethod = clazz.getMethod(c_methodName);
        if (c_fieldMethod != null)
        {
            Object c_value = c_fieldMethod.invoke(v_data);
            if (!String.valueOf(c_value).equals(c_fieldValue))
            {
                System.out.println("Not match condition");
            }else
            {
                System.out.println("match condition");
            }
        }

    }

    public static void testSqlPage() throws Exception
    {
        long t1 = System.currentTimeMillis();
        String sql = new StringBuilder()
                .append("SELECT distinct T.ID,T.TBDWMC,T.COMPANYID,T.SYDWMC,T.BH,T.AJBH,LK.LKSJ,T.AJID,T.SXCS,T.SXAMOUNT,")
                .append("RY.XM XM,RY.SFZH,T.CJSJ,T.SYKSSJ,DECODE(RY.XB,'1','女','0','男','未知') XB,T.JRBAQSJ,T.ZBMJ,T.XBMJ,T.BAQCSID,T.BAQCSMC")
                .append(" FROM ZFGCGL_BAQSYQKDJ T JOIN ZFGCGL_RYXX RY ON RY.ID=T.RY_ID LEFT JOIN ZFGCGL_LKBAQDJ LK ON LK.BAQSYQKID=T.ID")
                .append(" JOIN (SELECT ORG.ID,ORG.ORGAN_CODE,ORG.ORGAN_NAME FROM SYS_ORGAN ORG WHERE ORG.SFYX_ST = '1' ")
                .append("START WITH ORG.ID = ? CONNECT BY PRIOR ORG.ID = ORG.PARENT_ORG) O ON (t.tbdwdm = O.ORGAN_CODE ")
                .append(" OR t.sydwdm = O.ORGAN_CODE OR t.xbdwdm = O.ORGAN_CODE OR t.baqdddm = O.ORGAN_CODE) ")
                .append("  WHERE T.SFYX='1' AND RY.SFYX='1'  ")
                .append("ORDER BY T.CJSJ DESC")
                .toString();

        List<Object> params = new ArrayList<>();
        params.add("1");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("pageNo", 1);
        map2.put("pageSize", 10);
        map2.put("oldPage", 2);
        map2.put("needGetTotal", true);

        System.out.println("Step:");
        Main main = new Main();
        for (int x= 1; x<=10; x++) {
            long t2 = System.currentTimeMillis();
            FastPagination rs = main.getPaginationBySql(sql, params, map2);
            long t3 = System.currentTimeMillis();

            System.out.println(t3 - t2);
            TimeUnit.SECONDS.sleep(2);
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
    }

    public FastPagination getPaginationBySql(CharSequence queryString, List params, Map pageParam)
    {
        if (null != pageParam.get("allPage") || null == pageParam.get("pageSize")) {
            FastPagination fastPagination = new FastPagination();

            fastPagination.setRows(jdbcTemplate.queryForList(queryString.toString(), params.toArray()));
            fastPagination.setTotal(fastPagination.getRows().size());
            return fastPagination;
        } else {
            Integer pageIndex = (Integer) pageParam.get("pageNo");
            Integer pageSize = (Integer) pageParam.get("pageSize");
            Integer oldPage = (Integer) pageParam.get("oldPage");
            String newSql = "select * from (" + queryString + ")";
            //顺序排列的ids
            String ascIds = null;
            if (null != pageParam.get("ascIds")) {
                ascIds = pageParam.get("ascIds").toString();
            }
            //降序排除的ids
            String descIds = null;
            if (null != pageParam.get("descIds")) {
                descIds = pageParam.get("descIds").toString();
            }
            if (StringUtils.isNotEmpty(ascIds)) {
                newSql += "order by " + ascIds + " asc nulls last";
            } else if (StringUtils.isNotEmpty(descIds)) {
                newSql += "order by " + descIds + " desc nulls last";
            } else {
                newSql = queryString.toString();
            }
            if (pageSize > 0) {
                String sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM( " +
                        newSql +
                        " ) A WHERE ROWNUM <= ? ) WHERE RN >= ?";
                List<Object> args = new ArrayList<>();
                if (null != params) {
                    args.addAll(params);
                }
                FastPagination fastPagination = new FastPagination();
                if (pageParam.get("needGetTotal").equals(true)) {
                    ForkJoinPool pool = new ForkJoinPool(1);
                    System.out.println(pool);
                    CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
                        String totalsql = "SELECT count(*) as rn FROM( " + queryString + " ) A";
                        Integer total = ((BigDecimal) jdbcTemplate.queryForMap(totalsql, args.toArray()).get("RN")).intValue();
                        return total;
                    }, pool);
//                    String totalsql = "SELECT count(*) as rn FROM( " + queryString + " ) A";
//                    Integer total = ((BigDecimal) jdbcTemplate.queryForMap(totalsql, args.toArray()).get("RN")).intValue();
                    try {
                        fastPagination.setTotal(cf.get());
//                        fastPagination.setTotal(total);
                    } catch (Exception e){
                        throw new RuntimeException("总数查询失败");
                    }
                }
                Integer pages;
                if (oldPage <= pageIndex) {//向前翻页
                    //end
                    args.add(pageIndex * pageSize + 1);
                    //start
                    args.add((oldPage - 1) * pageSize + 1);
                    pages = pageIndex - oldPage + 1;
                } else {      //向后翻页
                    //end
                    args.add(oldPage * pageSize + 1);
                    //start
                    args.add((pageIndex - 1) * pageSize + 1);
                    pages = oldPage - pageIndex + 1;
                }
                List rows = jdbcTemplate.queryForList(sql, args.toArray());
                fastPagination.setPageSize(pageSize);
                fastPagination.setRows(rows);
                if (rows.size() <= pageSize * pages) {
                    fastPagination.setHasNext(false);
                } else {
                    fastPagination.setHasNext(true);
                }
                return fastPagination;
            } else {
                List rows = jdbcTemplate.queryForList(newSql, params.toArray());
                FastPagination fastPagination = new FastPagination();
                fastPagination.setRows(rows);
                return fastPagination;
            }
        }
    }
}

class FastPagination<T> implements Serializable {

    /**
     * 当前页
     */
    private int pageCurrent;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 是否有下一条
     */
    private boolean hasNext;

    /**
     * 对象列表
     */
    private List<T> rows;

    /**
     * 总页数
     */
    private Integer total;

    public FastPagination(){

    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }
}

