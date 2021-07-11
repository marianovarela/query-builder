package ar.com.qbuilder.domain;

import java.math.BigDecimal;
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
	
	public Dataset<Row> getDataset(){
		return this.dataset;
	}
	
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
	
	public BigDecimal sum(String columnName) {
		try {
			Column sum = functions.sum(this.dataset.col(columnName));
			@SuppressWarnings("rawtypes")
			Dataset sumSet = this.dataset;
			sumSet = sumSet.select(sum);
			List<Row> result = sumSet.collectAsList();
			Row row = result.get(0);
			return row.getDecimal(0);
		} catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	
	public BigDecimal min(String columnName) {
		try {
			Column min = functions.min(this.dataset.col(columnName));
			@SuppressWarnings("rawtypes")
			Dataset minSet = this.dataset;
			minSet = minSet.select(min);
			List<Row> result = minSet.collectAsList();
			Row row = result.get(0);
			return row.getDecimal(0);
		} catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	
	public BigDecimal max(String columnName) {
		try {
			Column max = functions.max(this.dataset.col(columnName));
			@SuppressWarnings("rawtypes")
			Dataset maxSet = this.dataset;
			maxSet = maxSet.select(max);
			List<Row> result = maxSet.collectAsList();
			Row row = result.get(0);
			return row.getDecimal(0);
		} catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	
}
