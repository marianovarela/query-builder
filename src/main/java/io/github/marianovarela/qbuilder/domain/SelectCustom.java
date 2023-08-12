package io.github.marianovarela.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectCustom extends SelectAbstract {
	
	private List<Column> selection = new LinkedList<Column>();
	
	private Entity entity;
	
	//private String condition;
	
	public void addToSelect(String from) {
		// if  @param to is null, this have not alias
		this.selection.add(Column.buildColumn(from, Optional.empty()));
	}
	
	public void addToSelect(String from, String alias) {
		// if  @param to is null, this have not alias
		Optional<String> optAlias = Optional.ofNullable(alias);
		this.selection.add(Column.buildColumn(from, optAlias));
	}
	
	public void addToSelect(String from, String alias, Aggregation agg) {
		// if  @param to is null, this have not alias
		this.selection.add(AggregationColumn.buildColumn(from, alias, agg));
	}
	
	public SelectCustom(Entity entity) {
		this.entity = entity;
	}
	
}
