package ar.com.qbuilder.helper;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import ar.com.qbuilder.domain.ResultSet;

public class ResultBuilder {

	public static ResultSet buildSuccess(Dataset<Row> result) {
		ResultSet res = new ResultSet();
		res.setDataset(result);
		res.setError(false);
		return res;
	}
	
	public static ResultSet buildSuccess() {
		ResultSet res = new ResultSet();
		res.setError(false);
		return res;
	}
	
	public static ResultSet buildError(String message) {
		ResultSet res = new ResultSet();
		res.setError(true);
		res.setMessage(message);
		return res;
	}
	
}
