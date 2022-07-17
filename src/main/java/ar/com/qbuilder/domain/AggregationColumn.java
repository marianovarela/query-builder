package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregationColumn extends Column{

	private Aggregation aggregation;
	
	public static AggregationColumn buildColumn(String colName, String alias, Aggregation agg) {
		AggregationColumn column = new AggregationColumn();
		column.setColumn(colName);
		column.setAlias(alias);
		column.setAggregation(agg);
		
		return column;
	}
	
}
