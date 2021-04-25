import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * MIT.
 * Author: wangxiaolei(王小雷).
 * Date:17-2-9.
 * Project:SparkPostgresqlJdbc.
 */
public class SparkPostgresqlJdbc {
    public static void main (String[] args) {

        SparkSession spark = SparkSession
                .builder()
                .appName("SparkPostgresqlJdbc")
                .config("spark.some.config.option","some-value")
                .master("local[1]")
                .getOrCreate();
        //启动runSparkPostgresqlJdbc程序
        runSparkPostgresqlJdbc(spark);

        spark.stop();

    }

    private static void runSparkPostgresqlJdbc(SparkSession spark){
        //new一个属性
        System.out.println("确保数据库已经开启，并创建了products表和插入了数据");
        Properties connectionProperties = new Properties();


        //增加数据库的用户名(user)密码(password),指定postgresql驱动(driver)
        System.out.println("增加数据库的用户名(user)密码(password),指定postgresql驱动(driver)");
        connectionProperties.put("user","test_crm");
        connectionProperties.put("password","test_crm_123");
        connectionProperties.put("driver","org.postgresql.Driver");



        //SparkJdbc读取Postgresql的products表内容
        System.out.println("SparkJdbc读取Postgresql的products表内容");
        Dataset<Row> jdbcDF = spark.read()
                .jdbc("jdbc:postgresql://10.157.93.151:25308/db_test_crm","ods_coalash_customer",connectionProperties).select("company_id","create_time");
        //显示jdbcDF数据内容
        jdbcDF.show();



        //将jdbcDF数据新建并写入newproducts,append模式是连接模式，默认的是"error"模式。
//        jdbcDF.write().mode("append")
//                .jdbc("jdbc:postgresql://localhost:5432/postgres","newproducts",connectionProperties);

    }
}