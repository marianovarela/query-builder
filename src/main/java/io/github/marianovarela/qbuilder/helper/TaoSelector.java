package io.github.marianovarela.qbuilder.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.config.Config;
import io.github.marianovarela.qbuilder.config.domain.Datasource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			log.debug(e.getMessage());
		}
		return datasource;
	}
	
	@PostConstruct
	private void postConstruct() {
		this.config = context.getBean(Config.class);
	}
        

}
