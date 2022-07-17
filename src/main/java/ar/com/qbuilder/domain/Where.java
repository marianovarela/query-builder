package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Where {

	private String filter;
	
	public Where() { }
	
	public Where(String filter) {
		this.filter = filter;
	}
	
}
