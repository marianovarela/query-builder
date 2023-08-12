package io.github.marianovarela.qbuilder.domain;


import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {

	private String column;
	
	private Optional<String> alias = Optional.empty();
	
	public static Column buildColumn(String colName, Optional<String> alias) {
		Column column = new Column();
		column.setColumn(colName);
		column.setAlias(alias);
		
		return column;
	}
	
}
