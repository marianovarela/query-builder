package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Insertion extends Query {

	private long id;
	
	private int type;
	
	private String table;
	
	private String object;
	
	private long leftId;
	
	private long rightId;
	
	public Insertion withObject(String object) {
		this.object = object;
		return this;
	}
	
	public Insertion withId(long id) {
		this.id = id;
		return this;
	}
	
	public Insertion withType(int type) {
		this.type = type;
		return this;
	}

	public Insertion withTable(String table) {
		this.table = table;
		return this;
	}

}
