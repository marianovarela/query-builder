package ar.com.qbuilder.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.Config;

@Service
public class TaoSelector {
	
	@Autowired
	private ApplicationContext context;
	
	private Config config;
	
	public long selectTao(long id) {
		return (id  % config.getArity());
	}
	
	@PostConstruct
	private void postConstruct() {
		this.config = context.getBean(Config.class);
	}
        

}
