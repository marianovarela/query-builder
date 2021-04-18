package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Insertion extends Query {

	private InsertionType insertionType;
	
	private long id;
	
	private int type;
	
	private Object object;
	
	private long leftId;
	
	private long rightId;
	
	
	public Insertion withObject(String table, long id, int type, Object object) {
		this.insertionType = InsertionType.Object;
		this.id = id;
		this.type = type;
		this.object = object;
		return this;
	}

	public Insertion withAssociation(String table, long leftId, int type, long rightId) {
		//TODO
		return this;
	}

}
