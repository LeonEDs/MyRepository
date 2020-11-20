package MyUtil.demo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FreeMarker模板写Word文件
{
    /**
     * 读取模板文件
     * @param fileDirectory 模板文件目录文件夹
     * @param fileName 模板文件名
     */
    public static Template readFile(String fileDirectory, String fileName) throws IOException
    {
        // 初始化配置文件
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        //设置编码
        configuration.setDefaultEncoding("utf-8");
        //加载文件
        configuration.setDirectoryForTemplateLoading(new File(fileDirectory));
        //加载模板
        return configuration.getTemplate(fileName);
    }

    /**
     * 写入文件数据 并保存文件
     * @param outFilePath 输出文件
     * @param template 模板
     * @param dataMap 数据
     */
    public static void writeFile(String outFilePath, Template template, Map<String, Object> dataMap)
            throws IOException, TemplateException
    {
        File docFile = new File(outFilePath);
        FileOutputStream fos = new FileOutputStream(docFile);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8),10240);
        template.process(dataMap,out);

        out.close();
    }

    public static void main(String[] args) throws IOException, TemplateException
    {
        AtomicLong sharpEleCost = new AtomicLong(0L);
        //需要进行文本替换的信息
        Map<String, Object> data = new HashMap<>();
        List<Map<String, String>> equipAlarmList = new ArrayList<>();
        List<Map<String, String>> energyAlarmList = new ArrayList<>();
        Map<String, String> equip1 = new HashMap<>();
        equip1.put("equipAlarmDate", "2020-1-1");
        equip1.put("equipName", "设备NAME");
        equip1.put("equipAlarmType", "电源故障报警");
        equip1.put("equipAlarmNum", "4");
        Map<String, String> equip2 = new HashMap<>();
        equip2.put("equipAlarmDate", "2020-11-11");
        equip2.put("equipName", "设备NAME2");
        equip2.put("equipAlarmType", "电源故障报警");
        equip2.put("equipAlarmNum", "2");
        equipAlarmList.add(equip1);
        equipAlarmList.add(equip2);
        Map<String, String> energy1 = new HashMap<>();
        energy1.put("energyAlarmDate", "2020-1-1");
        energy1.put("energyType", "电力");
        energy1.put("energyAlarmKind", "告警");
        energy1.put("energyAlarmType", "用电量达4567千瓦时，接近阈值4589");
        energy1.put("energyAlarmNum", "4");
        Map<String, String> energy2 = new HashMap<>();
        energy2.put("energyAlarmDate", "2020-11-11");
        energy2.put("energyType", "热力");
        energy2.put("energyAlarmKind", "预警");
        energy2.put("energyAlarmType", "用电量达4567千瓦时，接近阈值4589");
        energy2.put("energyAlarmNum", "2");
        energyAlarmList.add(energy1);
        energyAlarmList.add(energy2);

        data.put("equipAlarmList", equipAlarmList);
        data.put("energyAlarmList", energyAlarmList);

        data.put("eleCostMonthly", 555);
        data.put("eleCostSharp", 213);
        data.put("eleCostPeak", 4123);
        data.put("eleCostOffPeak", 312);
        data.put("eleCostYoy", "12%");
        data.put("eleCostMom", "32%");
        data.put("suggest1", "XXXXXXXXXXXXXXXXXXXXXXXXX");

        data.put("standardFactor", 23.11);
        data.put("tureFactor", 21.21);
        data.put("suggest2", "YYYYYYYYYYYYYYYYYYYYYYYYY");

        data.put("heatCompress", "");
        data.put("heatFlowRate", "");
        data.put("heatTemperature", "");
        data.put("suggest3", "");

        data.put("gasCompress", "");
        data.put("gasFlowRate", "");
        data.put("gasTemperature", "");
        data.put("suggest4", "");

        data.put("cpCompress", "");
        data.put("cpFlowRate", "");
        data.put("cpTemperature", "");
        data.put("suggest5", "");

        String relationship = "rId";
        int relationship_id = 9;//

        PictureParamObject pic = new PictureParamObject();
        pic.readFile(new File("F:/tmp/u1093.png"));
        byte[] imgData = pic.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String imageYoy = encoder.encode(imgData);
        Map<String, String> _map = new HashMap<>();
        _map.put("image_name", pic.getFile().getName());
        _map.put("image_base64",imageYoy);
        _map.put("image_type", "image/" + pic.getFileType());
        _map.put("relationship_id", relationship+relationship_id);
        data.put("picA", _map);

        PictureParamObject pic2 = new PictureParamObject();
        pic2.readFile(new File("F:/tmp/u1101.png"));
        byte[] imgData2 = pic2.toByteArray();
        String imageMom = encoder.encode(imgData2);
        Map<String, String> _map2 = new HashMap<>();
        _map2.put("image_name", pic2.getFile().getName());
        _map2.put("image_base64",imageMom);
        _map2.put("image_type", "image/" + pic2.getFileType());
        _map2.put("relationship_id", relationship+(++relationship_id));
        data.put("picB", _map2);


        Template template = FreeMarker模板写Word文件.readFile("F:/tmp/","EnergyFormMould.xml");
        FreeMarker模板写Word文件.writeFile("F:/tmp/replace.docx", template, data);
    }
}
