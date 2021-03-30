package ar.com.qbuilder.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Query {
	
	private String table;

	public Query() { }
	
	public Query(String table) {
		this.table = table;
	}
	
}
