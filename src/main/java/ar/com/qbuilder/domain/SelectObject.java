package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectObject extends Select{

	private Long id;
	
	private Object data;
	
	private String table = "objects";
	
	public SelectObject(String table) {
		super(table);
	}

	public SelectObject() {
		super();
	}

}
