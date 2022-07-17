package io.github.marianovarela.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Join extends SelectAbstract {

	private Subquery from;
	
	private Subquery to;
	
	private String selection; 
	
	private List<Condition> joinClause = new LinkedList<Condition>();

	//private String filter;
	
	private JoinType type;
	
	public Join build() {
		Join join = new Join();
		return join;
	}
	
	public Join withFrom(SelectCustom select) {
		Subquery subquery = Subquery.build()
				.setSelect(select);
		this.setFrom(subquery);
		return this;
	}
	
	public Join withFrom(ResultSet resultSet) {
		Subquery subquery = Subquery.build()
				.setResultSet(resultSet);
		this.setFrom(subquery);
		return this;
	}
	
	public Join withTo(SelectCustom select) {
		Subquery subquery = Subquery.build()
				.setSelect(select);
		this.setTo(subquery);
		return this;
	}
	
	public Join withTo(ResultSet resultSet) {
		Subquery subquery = Subquery.build()
				.setResultSet(resultSet);
		this.setTo(subquery);
		return this;
	}
}
