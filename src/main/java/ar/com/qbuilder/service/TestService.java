package ar.com.qbuilder.service;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class TestService {

	public void test() {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		String query = "select * from prueba.post_blocks where id = 1";
//		com.mysql.jdbc.Driver
		Dataset<Row> jdbcDF = spark.read()
				  .format("jdbc")
				  .option("url", "jdbc:mysql://192.168.6.250:13317")
				  .option("driver", "com.mysql.jdbc.Driver")
				  .option("dbtable", "prueba.processed_blocks")
				  .option("user", "root")
				  .option("password", "root")
				  .load();
		
		List<Row> rows = jdbcDF.collectAsList();
		for (Row row : rows) {
			System.out.println(row.toString());
		}
		
		Dataset<Row> jdbcDF2 = spark.read()
				  .format("jdbc")
				  .option("url", "jdbc:mysql://192.168.6.250:13317")
				  .option("driver", "com.mysql.jdbc.Driver")
				  .option("dbtable", "prueba.processed_blocks")
				  .option("user", "root")
				  .option("password", "root")
				  .load();
		
		Dataset<Row> joined = jdbcDF.join(jdbcDF2, jdbcDF.col("old_id").equalTo(jdbcDF2.col("old_id")), "inner");
		long count = joined.count();
		System.out.println("son tantos joins " + count);
		joined.show();
		joined.printSchema();
		
		spark.stop();
				
		
	}

}
