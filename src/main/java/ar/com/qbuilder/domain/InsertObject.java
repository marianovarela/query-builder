package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertObject extends Query {

	private Long id;
	
	private String data;
	
	public InsertObject withObject(String object) {
		this.data = object;
		return this;
	}
	
	public InsertObject withId(long id) {
		this.id = id;
		return this;
	}
	
	
	public String getTable() {
		return "objects";
	}
}
