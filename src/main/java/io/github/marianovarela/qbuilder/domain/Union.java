package io.github.marianovarela.qbuilder.domain;

import java.util.Optional;

import lombok.Getter;

@Getter
public class Union extends SelectAbstract {

	private Subquery first;
	
	private Subquery second;
	
	private Optional<String> selection; 
	
	//private String filter;		
	
	public Union(SelectCustom first, SelectCustom second) {
		Subquery firstSubquery = Subquery.build()
				.setSelect(first);
		Subquery secondSubquery = Subquery.build()
				.setSelect(second);
		this.first = firstSubquery;
		this.second = secondSubquery;
	}
	
	public Union(SelectCustom first, ResultSet second) {
		Subquery firstSubquery = Subquery.build()
				.setSelect(first);
		Subquery secondSubquery = Subquery.build()
				.setResultSet(second);
		this.first = firstSubquery;
		this.second = secondSubquery;
	}
	
	public Union(ResultSet first, SelectCustom second) {
		Subquery firstSubquery = Subquery.build()
				.setResultSet(first);
		Subquery secondSubquery = Subquery.build()
				.setSelect(second);
		this.first = firstSubquery;
		this.second = secondSubquery;
	}
	
	public Union(ResultSet first, ResultSet second) {
		Subquery firstSubquery = Subquery.build()
				.setResultSet(first);
		Subquery secondSubquery = Subquery.build()
				.setResultSet(second);
		this.first = firstSubquery;
		this.second = secondSubquery;
	}

	public void setSelection(String selection) {
		Optional<String> optSelection = Optional.ofNullable(selection);
		this.selection = optSelection;
	}
	
}
