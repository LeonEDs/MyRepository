import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SimpleJdbc {
    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .master("local[1]")
                .getOrCreate();
//        Dataset<Row> df = spark.read().json("/Users/gongyufeng/myproject/files/people.json");


//显示jdbcDF数据内容
        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", "jdbc:postgresql://10.157.93.151:25308/db_test_crm")
                .option("dbtable", "ods_coalash_customer")
                .option("user", "test_crm")
                .option("password", "test_crm_123")
                .load();
        jdbcDF.show();
        jdbcDF.createOrReplaceTempView("fccustomername");
        System.out.println("临时视图输出记录");
        Dataset<Row> sqlDF1 = spark.sql("SELECT fc_customer_name FROM fccustomername");
        sqlDF1.show();
//        新建全局视图，使用是用前缀global_temp即可
        jdbcDF.createGlobalTempView("fccustomernameview");
        Dataset<Row> sqlDF = spark.sql("SELECT fc_customer_name FROM global_temp.fccustomernameview");
        System.out.println("全局视图输出记录");
        sqlDF.show();
        spark.stop();
    }
}
