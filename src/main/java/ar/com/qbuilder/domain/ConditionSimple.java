package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConditionSimple extends Condition{

	private String operator;
	
	// lo que tenga que comparar
	private Entity entity;
	
	private String field;
	
	private String value;
	
}
