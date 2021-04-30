package ar.com.qbuilder.service;

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
import ar.com.qbuilder.domain.Association;

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

	public void writeObject(Datasource datasource, String table, List<ar.com.qbuilder.domain.Object> objects) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
		
		JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(objects);
        Dataset<Row> objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);

        Properties properties = new java.util.Properties();
        objDf.write().mode(SaveMode.Append).jdbc("jdbc:mysql://192.168.5.143:3306/" + datasource.getSchema() + "?user=" 
        		+ datasource.getUser() 
        		+ "&password=" 
        		+ datasource.getPassword(), 
        		table, properties);
        
        
	}

	public void writeAssociation(Datasource datasource, String table, List<Association> list) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
		
		JavaRDD<ar.com.qbuilder.domain.Association> associationRDD = jsc.parallelize(list);
        Dataset<Row> associationDf = sqlContext.createDataFrame(associationRDD, ar.com.qbuilder.domain.Association.class);

        Properties properties = new java.util.Properties();
        associationDf.write().mode(SaveMode.Append).jdbc("jdbc:mysql://192.168.5.143:3306/" + datasource.getSchema() + "?user=" 
        		+ datasource.getUser() 
        		+ "&password=" 
        		+ datasource.getPassword(), 
        		table, properties);
	}

}
