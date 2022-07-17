package io.github.marianovarela.qbuilder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.github.marianovarela.qbuilder.config.domain.Datasource;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("tao")
@Getter
@Setter
public class Config {

	@Autowired
	private Environment env;
	
	public Datasource getDatasource(long idTao) throws Exception {
		if(idTao > this.getArity()) {
			throw new Exception("El tao ingresado no corresponde");
		}else {
			return new Datasource(
				env.getProperty("tao" + idTao + ".url"),
				env.getProperty("tao" + idTao + ".driver"),
				env.getProperty("tao" + idTao + ".user"),
				env.getProperty("tao" + idTao + ".password"),
				env.getProperty("tao" + idTao + ".schema")
			);
		}
	}
	
	public int getArity() {
		int current = 0;
		boolean exist = true;
		while(exist) {
			String url = env.getProperty("tao" + current + ".url");
			if(url != null) {
				current += 1;				
			} else {
				exist = false;
			}
		}
		return current;
	}

	public String getURL(int nTao) {
		return env.getProperty("tao" + nTao + ".url");
	}
	
}