import com.xad.demo.beans.AspectDemo;
import com.xad.demo.config.SpringConfig;
import com.xad.demo.mapper.ExecuteMapper;
import com.xad.demo.service.ExecuteService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/18
 */
public class SpringStartTest
{
    @Test
    public void testAspect()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        AspectDemo demo = context.getBean(AspectDemo.class);

        demo.div(1f, 2f);
        System.out.println(demo);
    }

    @Test
    public void testQueryMybatis()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ExecuteService demo = context.getBean(ExecuteService.class);

        List<Map<String, Object>> rs = demo.queryXxlJobInfo("2", "客户数据同步");
        System.out.println(rs);
    }

    @Test
    public void testUpdateMybatis()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ExecuteService demo = context.getBean(ExecuteService.class);

        int rs = demo.updateXxlJobInfo("9", "author", "ZJHC16");
        System.out.println(rs);
    }
}
