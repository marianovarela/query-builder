package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertObject extends Query {

	private Long id;
	
	private String data;
	
	public InsertObject(Long id, Long type) {
		this.id = id;
		this.type = type;
	}
	
	public String getTable() {
		return "objects";
	}
}
