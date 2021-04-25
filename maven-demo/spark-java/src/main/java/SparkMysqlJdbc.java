import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * spark sql链接mysql数据源
 */
public class SparkMysqlJdbc {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .master("local[1]")
                .getOrCreate();
        Dataset<Row> df = spark.read().csv("/Users/gongyufeng/myproject/files/stf_gg_sale_risk.csv");

        df.show();
        df.write()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306")
                .option("dbtable", "dbgirl.stf_gg_sale_risk")
                .option("user", "root")
                .option("password", "88888888")
                .save();
//        df.describe("age").show();
//        df.filter("age is not null").show();

//        df.select("name")
//                .write()
//                .mode("append")
//                .format("jdbc")
//                .option("url", "jdbc:mysql://localhost:3306")
//                .option("dbtable", "dbgirl.people")
//                .option("user", "root")
//                .option("password", "88888888")
//        .save();


    }
}
