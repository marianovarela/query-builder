package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {

	private String column;
	
	private String alias;
	
	public static Column buildColumn(String colName, String alias) {
		Column column = new Column();
		column.setColumn(colName);
		column.setAlias(alias);
		
		return column;
	}
	
}
