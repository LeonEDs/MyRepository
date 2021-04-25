import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.collection.Seq;

import static com.mysql.cj.xdevapi.Expression.expr;

/**
 * spark sql 创建临时视图执行sql语句
 */
public class YY {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .master("local[1]")
                .getOrCreate();
        Dataset<Row> df = spark.read()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306")
                .option("dbtable", "dbgirl.stf_gg_sale_risk")
                .option("user", "root")
                .option("password", "88888888")
                .load();
//        df.createOrReplaceTempView("dfv");
//        spark.sql("select * from dfv").show();
//        spark.sql("select id,bukrs,comp_name,pivot(sum('cash_feb') for 'cash_feb' in select ) as ('cash_feb','debt_jan') from dfv").show();

        df.filter("cash_feb > debt_jan and debt_jan > 0").createOrReplaceTempView("dfv");
        spark.sql("select id||'01' as id ,bukrs,gjahr,monat,'1月份' as month,debt_jan as debt,cash_jan as cash from dfv union all " +
                "select id||'02' as id ,bukrs,gjahr,monat,'2月份' as month,debt_feb as debt,cash_feb as cash from dfv union all " +
                "select id||'03' as id ,bukrs,gjahr,monat,'3月份' as month,debt_mar as debt,cash_mar as cash from dfv").show();
//        spark.sql("select comp_name,cash_feb,debt_jan,risk_count from dfv").show();
        System.out.println("+++++++++++++++++++++++++++++++");
        spark.sql("select comp_name,cash_feb,debt_jan,risk_count+1 from dfv").show();
//        .select("comp_name","cash_feb","debt_jan",nvl(risk_count,risk_count+1)).show();
//                        （df.("risk_count")+1）).show();
//                select("comp_name","cash_feb","debt_jan",expr(su"risk_count").show();
//        df.show();

//
        spark.stop();
    }

}
