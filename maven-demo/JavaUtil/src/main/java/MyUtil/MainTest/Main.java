package MyUtil.MainTest;


import MyUtil.NumericUtil.NumericCheckUtils;
import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.Arrays;

public class Main
{
    private final static String tagIdKey = "tagId=";

    @SuppressWarnings("resource")
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException
    {
        String param = "tagId=3;";
        if (StringUtils.isNotEmpty(param))
        {
            String[] params = param.split(";");
            Long tagId = null;
            for (String p : params)
            {
                if (p.startsWith(tagIdKey))
                {
                    int i = p.indexOf(tagIdKey);
                    String tagIdStr = p.substring(i+tagIdKey.length());
                    boolean isN =  NumericCheckUtils.isPositiveInteger(tagIdStr);
                    if (StringUtils.isNotEmpty(tagIdStr) && isN)
                    {
                        tagId = Long.valueOf(tagIdStr);
                    }
                }
            }
        }
    }
}
