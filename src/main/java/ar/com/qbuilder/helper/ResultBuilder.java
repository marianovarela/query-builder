package ar.com.qbuilder.helper;

import ar.com.qbuilder.domain.Result;

public class ResultBuilder {

	public static Result buildSuccess(Object result) {
		return new Result(true, result);
	}
	
	public static Result buildSuccess() {
		return new Result(true);
	}
	
	public static Result buildError(String message) {
		return new Result(false, message);
	}
	
}
