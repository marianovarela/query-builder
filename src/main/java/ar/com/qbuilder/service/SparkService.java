package ar.com.qbuilder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;

@Component
public class SparkService {

	public SparkSession getOrCreate() {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		return spark;
	}
	
	public void run(String query, String table) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
//		String query = "select * from prueba.post_blocks where id = 1";
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
	}

	public void run(String query, Datasource datasource, String table) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
//		Dataset<Row> jdbcDF = spark.sqlContext().sql(query);
//		Dataset<Row> jdbcDF = 
//					spark.read()
//				  .format("jdbc")
//				  .option("url", datasource.getUrl())
//				  .option("driver", datasource.getDriver())
////				  .option("dbtable", datasource.getSchema() + "." + table)
//				  .option("user", datasource.getUser())
//				  .option("password", datasource.getPassword())
//				  .sql
//				  .load();
		
		List<ar.com.qbuilder.domain.Object> list = new ArrayList<>();
		ar.com.qbuilder.domain.Object obj = new ar.com.qbuilder.domain.Object();
		obj.setId(123);
		obj.setData("'{}'");
		obj.setType(10);
		list.add(obj);
		
		SQLContext sqlContext = spark.sqlContext();
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

		JavaRDD<ar.com.qbuilder.domain.Object> personsRDD = jsc.parallelize(list);
        Dataset<Row> userDf = sqlContext.createDataFrame(personsRDD, ar.com.qbuilder.domain.Object.class);

        Properties properties = new java.util.Properties();
//        properties.setProperty("user", datasource.getUser());
//        properties.setProperty("pass", datasource.getPassword());
//        properties.setProperty("driver", datasource.getDriver());
//        userDf.write().mode(SaveMode.Append).jdbc(datasource.getUrl() + "/tao3", table, properties);
        userDf.write().mode(SaveMode.Append).jdbc("jdbc:mysql://192.168.5.143:3306/tao3?user=root&password=root", table, properties);

//        jdbc:mysql://192.168.5.143:13317/tao3?user=root&password=root&useUnicode=true&characterEncoding=UTF-8";
				
//		List<Row> rows = jdbcDF.collectAsList();
//		for (Row row : rows) {
//			System.out.println(row.toString());
//		}
	}

}
