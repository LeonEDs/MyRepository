import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Jdtf_gg_sale_balance {
    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .master("local[1]")
                .getOrCreate();

//显示jdbcDF数据内容
        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", "jdbc:postgresql://10.157.93.151:25308/db_test_crm")
                .option("dbtable", "dtf_gg_sale_balance")
                .option("user", "test_crm2")
                .option("password", "test_crm_123")
                .load();
        jdbcDF.show();
//        jdbcDF.createOrReplaceTempView("fccustomername");
//        System.out.println("临时视图输出记录");
//        Dataset<Row> sqlDF1 = spark.sql("SELECT * FROM fccustomername");
//        sqlDF1.show();

        spark.stop();
    }
}
