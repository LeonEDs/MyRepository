
import com.demo.MainApplication;
import com.demo.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MainApplication.class)
public class JUnitTest
{
    @Autowired
    DemoService service;

    @Test
    public void execSQL()
    {
        List<Map<String, Object>> res = service.querySQL();
        System.out.println(res.get(res.size() - 1));
    }
}
