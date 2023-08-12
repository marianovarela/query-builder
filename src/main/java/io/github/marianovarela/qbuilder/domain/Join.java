package io.github.marianovarela.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Join extends SelectAbstract {

	private Subquery from;
	
	private Subquery to;
	
	private Optional<String> selection; 
	
	private List<Condition> joinClause = new LinkedList<Condition>();

	//private String filter;
	
	private JoinType type;
	
	public Join(SelectCustom from, SelectCustom to, JoinType type) {
		Subquery fromSubquery = Subquery.build()
				.setSelect(from);
		Subquery toSubquery = Subquery.build()
				.setSelect(to);
		this.from = fromSubquery;
		this.to = toSubquery;
		this.type = type;
	}
	
	public Join(SelectCustom from, ResultSet to, JoinType type) {
		Subquery fromSubquery = Subquery.build()
				.setSelect(from);
		Subquery toSubquery = Subquery.build()
				.setResultSet(to);
		this.from = fromSubquery;
		this.to = toSubquery;
		this.type = type;
	}
	
	public Join(ResultSet from, SelectCustom to, JoinType type) {
		Subquery fromSubquery = Subquery.build()
				.setResultSet(from);
		Subquery toSubquery = Subquery.build()
				.setSelect(to);
		this.from = fromSubquery;
		this.to = toSubquery;
		this.type = type;
	}
	
	public Join(ResultSet from, ResultSet to, JoinType type) {
		Subquery fromSubquery = Subquery.build()
				.setResultSet(from);
		Subquery toSubquery = Subquery.build()
				.setResultSet(to);
		this.from = fromSubquery;
		this.to = toSubquery;
		this.type = type;
	}
	
	public void setSelection(String selection) {
		Optional<String> optSelection = Optional.ofNullable(selection);
		this.selection = optSelection;
	}
	
}
