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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Association;
import ar.com.qbuilder.domain.Select;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.helper.FilterBuilder;

@Component
public class SparkService {

	@Autowired
	private FilterBuilder filterBuilder;
	
	public SparkSession getOrCreate() {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		return spark;
	}

	public void run(String query, String table) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
//		String query = "select * from prueba.post_blocks where id = 1";
		Dataset<Row> jdbcDF = spark.read().format("jdbc").option("url", "jdbc:mysql://192.168.6.250:13317")
				.option("driver", "com.mysql.jdbc.Driver").option("dbtable", "prueba.processed_blocks")
				.option("user", "root").option("password", "root").load();

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
				+ datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);

	}
	
	public Object updateObject(Datasource datasource, String table, List<ar.com.qbuilder.domain.Object> objects) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

		JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(objects);
		Dataset<Row> objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);

		Properties properties = new java.util.Properties();
		objDf.write().mode(SaveMode.Overwrite).jdbc(datasource.getUrl() + "/" + datasource.getSchema() + "?user="
				+ datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);
		
		return null;
	}

	public void writeAssociation(Datasource datasource, String table, List<Association> list) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

		JavaRDD<ar.com.qbuilder.domain.Association> associationRDD = jsc.parallelize(list);
		Dataset<Row> associationDf = sqlContext.createDataFrame(associationRDD,
				ar.com.qbuilder.domain.Association.class);

		Properties properties = new java.util.Properties();
		associationDf.write().mode(SaveMode.Append).jdbc(datasource.getUrl() + "/" + datasource.getSchema()
				+ "?user=" + datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);
	}
	
	public void updateAssociation(Datasource datasource, String table, List<Association> list) {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

		JavaRDD<ar.com.qbuilder.domain.Association> associationRDD = jsc.parallelize(list);
		Dataset<Row> associationDf = sqlContext.createDataFrame(associationRDD,
				ar.com.qbuilder.domain.Association.class);

		Properties properties = new java.util.Properties();
		associationDf.write().mode(SaveMode.Overwrite).jdbc(datasource.getUrl() + "/" + datasource.getSchema()
				+ "?user=" + datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);
	}

	public void delete() {
		SparkSession spark = this.getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		
		Properties connectionProperties = new Properties();
	    connectionProperties.put("user", "root");
	    connectionProperties.put("password", "root");

	    Dataset<Row> jdbcDF2 = spark.read()
	              .jdbc("jdbc:mysql://192.168.5.143:3306/" + "tao2"
	  				+ "?user=" + "root" + "&password=" + "root", "associations", connectionProperties);
		jdbcDF2.createOrReplaceTempView("associations");
		sqlContext.sql("DELETE FROM associations WHERE left_id = 152 and type = 10 and right_id = 153;");
	}

	public Object execute(Datasource datasource, SelectAssociation select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select, spark);
		
		String filter = buildFilter(select);
		
		jdbcDF = jdbcDF.filter(filter);
		if(select.isCount()) {
			return jdbcDF.count();
		}
		
		Object result = (Object) jdbcDF.collect(); 
		return result;
	}

	private Dataset<Row> getDataFrame(Datasource datasource, Select select, SparkSession spark) {
		Dataset<Row> jdbcDF = spark.read()
				  .format("jdbc")
				  .option("url", datasource.getUrl())
				  .option("driver", datasource.getDriver())
				  .option("dbtable", datasource.getSchema() + "." + select.getTable())
				  .option("user", datasource.getUser())
				  .option("password", datasource.getPassword())
				  .load();
		return jdbcDF;
	}

	private String buildFilter(SelectAssociation select) {
		String filter = "";
		if(select.getLeftId() != null) {
			filter = filterBuilder.addFilter("left_id", select.getLeftId().toString(), filter);
		}
		if(select.getType() != null) {
			filter = filterBuilder.addFilter("type", select.getType().toString(), filter);
		}
		if(select.getRightId() != null) {
			filter = filterBuilder.addFilter("right_id", select.getRightId().toString(), filter);
		}
		return filter;
	}

	private String buildFilter(SelectObject select) {
		String filter = "";
		if(select.getId() != null) {
			filter = filterBuilder.addFilter("id", select.getId().toString(), filter);
		}
		return filter;
	}

	public Object execute(Datasource datasource, SelectObject select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select, spark);
		
		String filter = buildFilter(select);
		
		jdbcDF = jdbcDF.filter(filter);
		if(select.isCount()) {
			return jdbcDF.count();
		}
				
		return jdbcDF.collect();
	}

}
