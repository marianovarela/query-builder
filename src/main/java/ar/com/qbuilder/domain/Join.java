package ar.com.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Join {

	private SelectCustom from;
	
	private SelectCustom to;
	
	private List<Condition> joinClause = new LinkedList<Condition>();

	private String filter;
	
	private JoinType type;
	
}
