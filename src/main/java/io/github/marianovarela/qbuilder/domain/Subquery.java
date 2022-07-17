package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Subquery {

	SelectCustom select;
	
	ResultSet resultSet;
	
	public static Subquery build() {
		Subquery subquery = new Subquery();
		return subquery;
	}
	
	public Subquery setSelect(SelectCustom select) {
		this.select = select;
		this.resultSet = null;
		return this;
	}
	
	public Subquery setResultSet(ResultSet resultSet) {
		this.select = null;
		this.resultSet = resultSet;
		return this;
	}
}
