package ar.com.qbuilder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Association;
import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.DeleteObject;
import ar.com.qbuilder.domain.Entity;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.helper.FilterBuilder;
import ar.com.qbuilder.utils.HibernateUtil;

@Component
public class SparkService {

	@Autowired
	private FilterBuilder filterBuilder;

	@Autowired
	private HibernateUtil hibernateUtil;

	private static String appName = "Sp_LogistcRegression";

	private static String master = "local";

	public SparkSession getOrCreate() {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master(master).getOrCreate();
		return spark;
	}

	public Dataset<Row> getEmptyDataSet(Entity entity) {
		SparkSession spark = getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		SparkContext sparkContext = spark.sparkContext();
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);
		Dataset<Row> objDf = null;
		if(entity.equals(Entity.Objects)) {
			JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(new ArrayList<ar.com.qbuilder.domain.Object>());
			objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);
		} else { //Entity.Associations
			JavaRDD<ar.com.qbuilder.domain.Association> objRDD = jsc.parallelize(new ArrayList<ar.com.qbuilder.domain.Association>());
			objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Association.class);
		}
		return objDf;
	}
	
	public Dataset<Row> getEmptyDataSet(SelectCustom select) {
		SparkSession spark = getOrCreate();
		SparkContext sparkContext = spark.sparkContext();
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);

//		JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(new ArrayList<ar.com.qbuilder.domain.Object>());
//		Dataset<Row> objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);
		List<StructField> fields = new ArrayList<>();
		for (Pair<String, String> pair : select.getSelection()) {
		  StructField field = DataTypes.createStructField(pair.getValue1(), DataTypes.StringType, true);
		  fields.add(field);
		}
		StructType schema = DataTypes.createStructType(fields);
		// Convert records of the RDD to Rows
		JavaRDD<Row> rowRDD = jsc.emptyRDD();

		// Apply the schema to the RDD
		Dataset<Row> objDf = spark.createDataFrame(rowRDD, schema);
		return objDf;
	}

	public void writeObject(Datasource datasource, String table, List<ar.com.qbuilder.domain.Object> objects) {
		SparkSession spark = SparkSession.builder().appName(appName).master(master).getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		SparkContext sparkContext = spark.sparkContext();
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);

		JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(objects);
		Dataset<Row> objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);

		Properties properties = new java.util.Properties();
		objDf.write().mode(SaveMode.Append).jdbc(datasource.getUrl() + "/" + datasource.getSchema() + "?user="
				+ datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);

	}

	public Object updateObject(Datasource datasource, String table, List<ar.com.qbuilder.domain.Object> objects) {
		SparkSession spark = SparkSession.builder().appName(appName).master(master).getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		SparkContext sparkContext = spark.sparkContext();
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);

		JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(objects);
		Dataset<Row> objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);

		Properties properties = new java.util.Properties();
		objDf.write().mode(SaveMode.Overwrite).jdbc(datasource.getUrl() + "/" + datasource.getSchema() + "?user="
				+ datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);

		return null;
	}

	public void writeAssociation(Datasource datasource, String table, List<Association> list) {
		SparkSession spark = SparkSession.builder().appName(appName).master(master).getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		SparkContext sparkContext = spark.sparkContext();
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);

		JavaRDD<ar.com.qbuilder.domain.Association> associationRDD = jsc.parallelize(list);
		Dataset<Row> associationDf = sqlContext.createDataFrame(associationRDD,
				ar.com.qbuilder.domain.Association.class);

		Properties properties = new java.util.Properties();
		associationDf.write().mode(SaveMode.Append).jdbc(datasource.getUrl() + "/" + datasource.getSchema() + "?user="
				+ datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);
	}

	public void updateAssociation(Datasource datasource, String table, List<Association> list) {
		SparkSession spark = SparkSession.builder().appName(appName).master(master).getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		SparkContext sparkContext = spark.sparkContext();
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);

		JavaRDD<ar.com.qbuilder.domain.Association> associationRDD = jsc.parallelize(list);
		Dataset<Row> associationDf = sqlContext.createDataFrame(associationRDD,
				ar.com.qbuilder.domain.Association.class);

		Properties properties = new java.util.Properties();
		associationDf.write().mode(SaveMode.Overwrite).jdbc(datasource.getUrl() + "/" + datasource.getSchema()
				+ "?user=" + datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);
	}

	public void delete(Datasource datasource, DeleteAssociation delete) {
		SessionFactory sessionFactory = hibernateUtil.getSessionFactory(datasource);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String sql = String.format("DELETE FROM associations where left_id = '%s' and right_id = %s and type = %s;",
				delete.getLeftId(), delete.getRightId(), delete.getType());
		session.createSQLQuery(sql).executeUpdate();
		session.getTransaction().commit();
		session.close();
	}

	public Dataset<Row> execute(Datasource datasource, SelectAssociation select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select.getTable(), spark);

		String filter = buildFilter(select);

		jdbcDF = jdbcDF.filter(filter);

		if (select.getLimit() != null) {
			jdbcDF = jdbcDF.limit(select.getLimit());
		}

//		Row[] result = (Row[]) jdbcDF.collect();
//
//		if (select.getRange() != null) {
//			result = Arrays.copyOfRange(result, select.getRange().getPosition(),
//					select.getRange().getPosition() + select.getRange().getLimit());
//		}
		return jdbcDF;
	}

	private Dataset<Row> getDataFrame(Datasource datasource, String table, SparkSession spark) {
		Dataset<Row> jdbcDF = spark.read().format("jdbc").option("url", datasource.getUrl())
				.option("driver", datasource.getDriver())
				.option("dbtable", datasource.getSchema() + "." + table)
				.option("user", datasource.getUser())
				.option("password", datasource.getPassword())
				.load();
		return jdbcDF;
	}

	private String buildFilter(SelectAssociation select) {
		String filter = "";
		if (select.getLeftId() != null) {
			filter = filterBuilder.addFilter("left_id", select.getLeftId().toString(), filter);
		}
		if (select.getType() != null) {
			filter = filterBuilder.addFilter("type", select.getType().toString(), filter);
		}
		if (select.getRightId() != null) {
			filter = filterBuilder.addFilter("right_id", select.getRightId().toString(), filter);
		}
		// time range filter
		if (select.getTimeRange() != null) {
			filter = filterBuilder.addTimeRange("time", select.getTimeRange(), filter);
		}

		return filter;
	}

	public Dataset<Row> execute(Datasource datasource, SelectCustom select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select.getEntity().value, spark);
		if(select.getCondition() != null) {
			jdbcDF = jdbcDF.filter(select.getCondition());
		}
		return jdbcDF;
	}
	
	private String buildFilterById(SelectObject select) {
		String filter = "";
		if (select.getId() != null) {
			filter = filterBuilder.addFilter("id", select.getId().toString(), filter);
		}
		return filter;
	}

	public Dataset<Row> execute(Datasource datasource, SelectObject select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select.getTable(), spark);

		String filter = buildFilterById(select);

		jdbcDF = jdbcDF.filter(filter);

		return jdbcDF;
	}

	public void delete(Datasource datasource, DeleteObject delete) {
		SessionFactory sessionFactory = hibernateUtil.getSessionFactory(datasource);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String sql = String.format("DELETE FROM objects where id = '%s';", delete.getId());
		session.createSQLQuery(sql).executeUpdate();
		session.getTransaction().commit();
		session.close();
	}

}
