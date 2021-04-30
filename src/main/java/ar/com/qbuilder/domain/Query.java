package ar.com.qbuilder.domain;

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
	
	public Query withType(int type) {
		this.type = type;
		return this;
	}

	public Query withTable(String table) {
		this.table = table;
		return this;
	}
	
}
