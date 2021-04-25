package zncrm;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Ods_erp_sale_TO_dtf_gg_sale_orddtl {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .master("local[1]")
                .getOrCreate();
//        通过JDBC加载部分数据、添加过滤条件
//        由于dbtable被用作SELECT语句的源。如果要填入子查询语句，则应提供别名
//        https://blog.csdn.net/lb812913059/article/details/83341537
        String tablename = "(select * from mscm_gg_crm.ods_gcgcrm_orderinfo c where syn_time = to_date('2021-03-20','yyyy-MM-dd')) temp";
        Dataset<Row> df = spark.read()
                .format("jdbc")
                .option("url", "jdbc:postgresql://10.162.122.72:5432/db_test_crm")
                .option("dbtable", tablename)
                .option("user", "test_crm2")
                .option("password", "test_crm_123")
                .load();
        df.createOrReplaceTempView("dfv");
        df=spark.sql("select cons_no from dfv");
        df.show();

        spark.stop();
    }
}
