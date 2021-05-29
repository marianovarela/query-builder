package ar.com.qbuilder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Association;
import ar.com.qbuilder.domain.Condition;
import ar.com.qbuilder.domain.ConditionSimple;
import ar.com.qbuilder.domain.ConditionTree;
import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.DeleteObject;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.helper.FilterBuilder;
import ar.com.qbuilder.utils.HibernateUtil;
import scala.collection.Seq;

@Component
public class SparkService {

	@Autowired
	private FilterBuilder filterBuilder;

	@Autowired
	private HibernateUtil hibernateUtil;

	private SQLContext sqlContext;

	private static String appName = "Sp_LogistcRegression";

	private static String master = "local";

	public SparkSession getOrCreate() {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master(master).getOrCreate();
		return spark;
	}

	public Dataset<Row> getEmptyDataSet() {
		SparkSession spark = getOrCreate();
		SQLContext sqlContext = spark.sqlContext();
		SparkContext sparkContext = spark.sparkContext();
		@SuppressWarnings("resource")
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);

		JavaRDD<ar.com.qbuilder.domain.Object> objRDD = jsc.parallelize(new ArrayList<ar.com.qbuilder.domain.Object>());
		Dataset<Row> objDf = sqlContext.createDataFrame(objRDD, ar.com.qbuilder.domain.Object.class);
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

	public Object execute(Datasource datasource, SelectAssociation select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select.getTable(), spark);

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

	@SuppressWarnings("unchecked")
	public Dataset<Row> execute(Datasource datasource, SelectCustom select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select.getEntity().value, spark);
		String filter = buildFilter(select.getCondition());
		jdbcDF = jdbcDF.filter(filter);
		return jdbcDF;
	}

	private String buildFilter(Condition condition) {
		String filter = "";
		if(condition instanceof ConditionSimple) {
			ConditionSimple c = (ConditionSimple) condition;
			filter = buildFilter(c, filter);
		}else if(condition instanceof ConditionTree) {
			ConditionTree tree = (ConditionTree) condition;
			filter = buildFilter(tree, filter);
		}
		return filter;
	}

	private String buildFilter(ConditionTree tree, String filter) {
		String conditionFilter = "(" + makeCondition(tree) + ")";
		if(filter.isEmpty()) {
			filter = conditionFilter;
		} else {
			filter = tree.getLogicOperator() + " " + conditionFilter;
		}
		return filter;
	}

	private String makeCondition(ConditionTree tree) {
		String condition = "";
		Condition initial = tree.getConditions().get(0);
		condition = buildFilter(initial);
		if(tree.getConditions().size() > 1) {
			for (int i = 1; i < tree.getConditions().size(); i++) {
				Condition childCondition = tree.getConditions().get(i);
				condition += " " + childCondition.getLogicOperator() + " " + buildFilter(childCondition);
			}
		}
		return condition;
	}

	private String buildFilter(ConditionSimple condition, String filter) {
		filter = filterBuilder.addFilter(condition.getField(), condition.getValue(), condition.getOperator(), filter);
		return filter;
	}

	private String buildFilterById(SelectObject select) {
		String filter = "";
		if (select.getId() != null) {
			filter = filterBuilder.addFilter("id", select.getId().toString(), filter);
		}
		return filter;
	}

	public Object execute(Datasource datasource, SelectObject select) {
		SparkSession spark = this.getOrCreate();
		Dataset<Row> jdbcDF = getDataFrame(datasource, select.getTable(), spark);

		String filter = buildFilterById(select);

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
