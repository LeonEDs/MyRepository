package com.demo.yaml;

import org.apache.commons.collections4.MapUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XAD on  2023/9/9 21:51
 */
public class YamlUtil {
    public static Map<String, List<Map<String, Object>>> getMonitorMap(boolean isReload) {
        Map<String, List<Map<String, Object>>> monitorMap = new HashMap<>();
        if (isReload || MapUtils.isEmpty(monitorMap)) {
            Map<String, Object> map = (Map) getYaml("C:/Code/MyProject/MyRepository/maven-demo/JavaUtil/src/main/java/com/demo/yaml/demo.yaml");
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() instanceof Map) {
                        Map<String, Object> module = (Map) entry.getValue();
                        List<Map<String, Object>> monitorList = new ArrayList<>();

                        for (Map.Entry<String, Object> moduleEntry : module.entrySet()) {
                            if (moduleEntry.getValue() instanceof Map) {
                                monitorList.add((Map) moduleEntry.getValue());
                            }
                        }

                        monitorMap.put(MapUtils.getString(module, "code"), monitorList);
                    }
                }
            }
        }

        return monitorMap;
    }

    /**
     * 根据配置变量实时获取配置文件中的值
     *
     * @param filePath 配置文件路径名
     * @return 配置值
     */
    public static Map getYaml(String filePath) {
        Yaml yaml = new Yaml();
        Map obj = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            obj = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
