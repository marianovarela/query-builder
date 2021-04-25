package ar.com.qbuilder.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.config.domain.Datasource;

@Service
public class TaoSelector {
	
	@Autowired
	private ApplicationContext context;
	
	private Config config;
	
	public long selectTao(long id) {
		return (id  % config.getArity());
	}
	
	public Datasource getDatasource(long idTao) {
		Datasource datasource = null;
		try {
			datasource = config.getDatasource(idTao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datasource;
	}
	
	@PostConstruct
	private void postConstruct() {
		this.config = context.getBean(Config.class);
	}
        

}
