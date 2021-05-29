package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectCustom extends Query{
	
	// entity to retrieve
	private Entity entity;
	
	private String condition;
	
}
