package ar.com.qbuilder.domain;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

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
	
}
