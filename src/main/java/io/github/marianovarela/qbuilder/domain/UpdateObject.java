package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateObject extends Query {

	private Long id;
	
	private String data;
	
	private static String table = "objects";
	
	public UpdateObject withObject(String object) {
		this.data = object;
		return this;
	}
	
	public UpdateObject withId(long id) {
		this.id = id;
		return this;
	}
	
}