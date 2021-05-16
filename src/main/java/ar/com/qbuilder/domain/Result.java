package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Result {

	private boolean status;
	
	private java.lang.Object result;
	
	private String message;
	
	public Result(boolean status, String message) {
		this.status = false;
		this.message = message;
	}
	
	public Result(boolean status, java.lang.Object result) {
		this.status = false;
		this.result = result;
	}
	
	public Result(boolean status) {
		this.status = status;
	}

}
