package io.github.marianovarela.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupBy {
	
	private String columns;
	
	private List<AggregationColumn> aggregations = new LinkedList<AggregationColumn>();
	
	private GroupBy() {	}
	
	public static GroupBy build() {
		GroupBy groupBy = new GroupBy();
		return groupBy;
	}
	
	public GroupBy setGroupBy(String columns) {
		this.columns = columns;
		return this;
	}
	
	public GroupBy addAggregation(AggregationColumn agg) {
		this.aggregations.add(agg);
		return this;
	}
}
