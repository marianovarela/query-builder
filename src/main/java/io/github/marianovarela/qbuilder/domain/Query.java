package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Query {
	
	protected String table;
	
	protected Long type;
	
	protected Query() {
		
	}
	
//	public query(string table) {
//		this.table = table;
//	}
	
}
