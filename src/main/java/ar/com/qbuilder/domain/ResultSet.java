package ar.com.qbuilder.domain;

import java.util.List;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

import ar.com.qbuilder.exception.BusinessException;
import lombok.Setter;

@Setter
public class ResultSet {
	
	private Dataset<Row> dataset; 
	
	private boolean status;

	private String message;
	
	public long count() {
		return this.dataset.count();
	}
	
	public List<Row> get() {
		return  this.dataset.collectAsList();
	}
	
	public boolean isStatus() {
		return this.status;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public long sum(String columnName) {
		try {
			Column sum = functions.sum(this.dataset.col(columnName));
			@SuppressWarnings("rawtypes")
			Dataset sumSet = this.dataset;
			sumSet = sumSet.select(sum);
			List<Row> result = sumSet.collectAsList();
			Row row = result.get(0);
			return row.getLong(0);
		} catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	
}
