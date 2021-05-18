package ar.com.qbuilder.service;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Association;
import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.DeleteObject;
import ar.com.qbuilder.domain.Select;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.helper.FilterBuilder;
import ar.com.qbuilder.utils.HibernateUtil;

@Component
public class SparkService {

	@Autowired
	private FilterBuilder filterBuilder;

	@Autowired
	private HibernateUtil hibernateUtil;

	public SparkSession getOrCreate() {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		return spark;
	}

	public void writeObject(Datasource datasource, String table, List<ar.com.qbuilder.domain.Object> objects) {
		try {
			SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
			SQLContext sqlContext = spark.sqlContext();
			JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
			
			JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(objects);
			Dataset<Row> objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);
			
			Properties properties = new java.util.Properties();
			objDf.write().mode(SaveMode.Append).jdbc(datasource.getUrl() + "/" + datasource.getSchema() + "?user="
					+ datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);
		} catch(Exception  e) {
			e.printStackTrace();
		}
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
		associationDf.write().mode(SaveMode.Append).jdbc(datasource.getUrl() + "/" + datasource.getSchema() + "?user="
				+ datasource.getUser() + "&password=" + datasource.getPassword(), table, properties);
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

	public Object execute(Datasource datasource, SelectAssociation select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select, spark);

		String filter = buildFilter(select);

		jdbcDF = jdbcDF.filter(filter);
		if (select.isCount()) {
			return jdbcDF.count();
		}

		if (select.getLimit() != null) {
			jdbcDF = jdbcDF.limit(select.getLimit());
		}

		Row[] result = (Row[]) jdbcDF.collect();

		if (select.getRange() != null) {
			result = Arrays.copyOfRange(result, select.getRange().getPosition(),
					select.getRange().getPosition() + select.getRange().getLimit());
		}
		return result;
	}

	private Dataset<Row> getDataFrame(Datasource datasource, Select select, SparkSession spark) {
		Dataset<Row> jdbcDF = spark.read().format("jdbc").option("url", datasource.getUrl())
				.option("driver", datasource.getDriver())
				.option("dbtable", datasource.getSchema() + "." + select.getTable())
				.option("user", datasource.getUser()).option("password", datasource.getPassword()).load();
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

	private String buildFilter(SelectObject select) {
		String filter = "";
		if (select.getId() != null) {
			filter = filterBuilder.addFilter("id", select.getId().toString(), filter);
		}
		return filter;
	}

	public Object execute(Datasource datasource, SelectObject select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select, spark);

		String filter = buildFilter(select);

		jdbcDF = jdbcDF.filter(filter);
		if (select.isCount()) {
			return jdbcDF.count();
		}

		return jdbcDF.collect();
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
