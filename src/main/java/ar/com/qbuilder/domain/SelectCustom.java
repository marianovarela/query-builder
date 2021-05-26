package ar.com.qbuilder.domain;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectCustom {
	
	// entity to retrieve
	private Entity entity;
	
	// 	
	private List<Condition> conditions = new LinkedList<Condition>();
}
