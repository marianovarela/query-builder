package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Select extends Query{

//	protected boolean count = false;
	
	public Select(String table) {
		super(table);
	}

	public Select() {
		super();
	}

//	public void withCount() {
//		this.count = true;
//	}
	
}
