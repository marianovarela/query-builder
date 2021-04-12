package ar.com.qbuilder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("tao")
@Getter
@Setter
public class Config {

	@Autowired
	private Environment env;
	
	private int arity;
	
	public Datasource getDatasource(int nTao) throws Exception {
		if(nTao > this.arity) {
			throw new Exception("El tao ingresado no corresponde");
		}else {
			return new Datasource(
				env.getProperty("tao" + nTao + ".url"),
				env.getProperty("tao" + nTao + ".driver"),
				env.getProperty("tao" + nTao + ".user"),
				env.getProperty("tao" + nTao + ".password")
			);
		}
	}
	
	public String getURL(int nTao) {
		return env.getProperty("tao" + nTao + ".url");
	}

}