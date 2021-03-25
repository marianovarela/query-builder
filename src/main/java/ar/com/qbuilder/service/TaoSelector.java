package ar.com.qbuilder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaoSelector {
	
	@Value("${tao.arity}")
	private long arity;
	
	public long selectTao(long id) {
		return (id  % arity);
	}

}
