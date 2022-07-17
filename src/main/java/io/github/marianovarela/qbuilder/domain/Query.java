package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Query {
	
	protected String table;
	
	protected Integer type;

	public Query() { }
	
	public Query(String table) {
		this.table = table;
	}
	
	public Query withType(Integer type) {
		this.type = type;
		return this;
	}

	public Query withTable(String table) {
		this.table = table;
		return this;
	}
	
}
