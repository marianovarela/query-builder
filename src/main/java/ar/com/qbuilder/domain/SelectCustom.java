package ar.com.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectCustom extends SelectAbstract {
	
	private List<Column> selection = new LinkedList<Column>();
	
	private String alias;
	
	// entity to retrieve
	private Entity entity;
	
	//private String condition;
	
	public void addToSelect(String from) {
		// if  @param to is null, this have not alias
		this.selection.add(Column.buildColumn(from, null));
	}
	
	public void addToSelect(String from, String alias) {
		// if  @param to is null, this have not alias
		this.selection.add(Column.buildColumn(from, alias));
	}
	
	public void addToSelect(String from, String alias, Aggregation agg) {
		// if  @param to is null, this have not alias
		this.selection.add(AggregationColumn.buildColumn(from, alias, agg));
	}
	
}
