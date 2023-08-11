package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateObject extends Query {

	private Long id;
	
	private String data;
	
	private static String table = "objects";
	
	public UpdateObject(Long id, String data) {
		this.id = id;
		this.data = data;
	}
	
}
