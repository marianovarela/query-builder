package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectObject extends Select{

	private Long id;
	
	private String table = "objects";
	
	public SelectObject(String table) {
		super(table);
	}

	public SelectObject() {
		super();
	}

}
