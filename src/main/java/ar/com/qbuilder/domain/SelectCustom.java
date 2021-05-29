package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectCustom {
	
	// entity to retrieve
	private Entity entity;
	
	// 	
	private Condition condition;
}
