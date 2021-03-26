package ar.com.qbuilder.service;

import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

@Service
public class SparkService {

	public SparkSession getOrCreate() {
		SparkSession spark = SparkSession.builder().appName("Sp_LogistcRegression").master("local").getOrCreate();
		return spark;
	}
}
