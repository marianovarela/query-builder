package ar.com.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import org.javatuples.Pair;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectCustom {
	
	private List<Pair<String, String>> selection = new LinkedList<Pair<String, String>>();
	
	private String alias;
	
	// entity to retrieve
	private Entity entity;
	
	private String condition;

	public void addToSelect(String from, String to) {
		// if  @param to is null, this have not alias
		this.selection.add(new Pair<String, String>(from, to));
	}
	
}
