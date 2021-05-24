package ar.com.qbuilder.helper;

import ar.com.qbuilder.domain.Result;

public class ResultBuilder {

	public static Result buildSuccess(Object result) {
		Result res = new Result();
		res.setResult(result);
		res.setStatus(true);
		return res;
	}
	
	public static Result buildSuccess() {
		Result res = new Result();
		res.setStatus(true);
		return res;
	}
	
	public static Result buildError(String message) {
		Result res = new Result();
		res.setStatus(false);
		res.setMessage(message);
		return res;
	}
	
}
