package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Union extends SelectAbstract {

	private Subquery first;
	
	private Subquery second;
	
	private String selection; 
	
	//private String filter;		
	
	public Union build() {
		Union join = new Union();
		return join;
	}
	
	public Union withFirst(SelectCustom select) {
		Subquery subquery = Subquery.build()
				.setSelect(select);
		this.setFirst(subquery);
		return this;
	}
	
	public Union withFirst(ResultSet resultSet) {
		Subquery subquery = Subquery.build()
				.setResultSet(resultSet);
		this.setFirst(subquery);
		return this;
	}
	
	public Union withSecond(SelectCustom select) {
		Subquery subquery = Subquery.build()
				.setSelect(select);
		this.setSecond(subquery);
		return this;
	}
	
	public Union withSecond(ResultSet resultSet) {
		Subquery subquery = Subquery.build()
				.setResultSet(resultSet);
		this.setSecond(subquery);
		return this;
	}
}
