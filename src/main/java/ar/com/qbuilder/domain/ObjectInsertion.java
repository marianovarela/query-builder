package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectInsertion extends Query {

	private Long id;
	
	private String object;
	
	public ObjectInsertion withObject(String object) {
		this.object = object;
		return this;
	}
	
	public ObjectInsertion withId(long id) {
		this.id = id;
		return this;
	}
	
}
