package ar.com.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupBy {
	
	private String columns;
	
	private List<AggregationColumn> aggs = new LinkedList<AggregationColumn>();
	
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
		this.aggs.add(agg);
		return this;
	}
}
