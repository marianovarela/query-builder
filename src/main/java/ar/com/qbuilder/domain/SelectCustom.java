package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectCustom {
	
	private String alias;
	
	// entity to retrieve
	private Entity entity;
	
	private String condition;
	
}
