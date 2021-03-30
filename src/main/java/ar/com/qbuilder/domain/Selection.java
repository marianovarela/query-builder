package ar.com.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Selection extends Query{

	private List<String> projections = new LinkedList<String>();
	
	public Selection(String table) {
		super(table);
	}

	public Selection() {
		super();
	}

	public Selection with(String projection) {
		this.projections.add(projection);
		return this;
	}
	
	public Selection with(List<String> projections) {
		this.projections.addAll(projections);
		return this;
	}
	
}
