package io.github.marianovarela.qbuilder.domain;


import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AggregationColumn extends Column{

	private Aggregation aggregation;
	
	public static AggregationColumn buildColumn(String colName, String alias, Aggregation aggregation) {
		AggregationColumn column = new AggregationColumn();
		column.setColumn(colName);
		Optional<String> optAlias = Optional.ofNullable(alias);
		column.setAlias(optAlias);
		column.setAggregation(aggregation);
		
		return column;
	}
	
}
