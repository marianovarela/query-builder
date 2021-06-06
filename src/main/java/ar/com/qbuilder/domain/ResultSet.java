package ar.com.qbuilder.domain;

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
	
	public Row[] get() {
		return (Row[]) this.dataset.collect();
	}
	
	public boolean isStatus() {
		return this.status;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
