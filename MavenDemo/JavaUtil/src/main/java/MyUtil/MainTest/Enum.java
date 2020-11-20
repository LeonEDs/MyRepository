package MyUtil.MainTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Enum
{
    public enum OrgColEnum
    {
        //行业
        INDUSTRY_BP("10020101", "生物制药"),
        INDUSTRY_PT("10020102", "涂料"),
        INDUSTRY_FC("10020103", "精细化工"),
        INDUSTRY_AMP("10020104", "汽车零部件"),
        INDUSTRY_WHL("10020105", "仓储物流"),
        //规模
        SCALELARGE("10020201", "特大型"),
        SCALEBIG("10020202", "大型"),
        SCALEMIDDLE("10020203", "中型"),
        //性质
        BUSINESSNATURECN("10020301", "国企"),
        BUSINESSNATUREFR("10020302", "外企"),
        BUSINESSNATURECF("10020303", "中外合资"),
        BUSINESSNATUREPS("10020304", "民营")
        ;
        //行业
        public static final String INDUSTRY_BP_CODE = "10020101";
        public static final String INDUSTRY_PT_CODE = "10020102";
        public static final String INDUSTRY_FC_CODE = "10020103";
        public static final String INDUSTRY_AMP_CODE = "10020104";
        public static final String INDUSTRY_WHL_CODE = "10020105";
        //规模
        public static final String SCALELARGE_CODE = "10020201";
        public static final String SCALEBIG_CODE = "10020202";
        public static final String SCALEMIDDLE_CODE = "10020203";
        //性质
        public static final String BUSINESSNATURECN_CODE = "10020301";
        public static final String BUSINESSNATUREFR_CODE = "10020302";
        public static final String BUSINESSNATURECF_CODE = "10020303";
        public static final String BUSINESSNATUREPS_CODE = "10020304";

        private String orgColCode;
        private String orgColName;

        OrgColEnum(String orgColCode, String orgColName)
        {
            this.orgColCode = orgColCode;
            this.orgColName = orgColName;
        }

        public String getOrgColCode()
        {
            return orgColCode;
        }

        public void setOrgColCode(String orgColCode)
        {
            this.orgColCode = orgColCode;
        }

        public String getOrgColName()
        {
            return orgColName;
        }

        public void setOrgColName(String orgColName)
        {
            this.orgColName = orgColName;
        }

        public static List<OrgColEnum> getIndustryMap()
        {
            return Arrays.stream(OrgColEnum.values()).filter(e -> e.getOrgColCode().startsWith("100201"))
                    .collect(Collectors.toList());
        }
    }
}
