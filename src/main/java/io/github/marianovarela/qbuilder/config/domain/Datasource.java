package io.github.marianovarela.qbuilder.config.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Datasource {

	private String url;
	
	private String driver;
	
	private String user;
	
	private String password;
	
	private String schema;
	
	public Datasource(String url, String driver, String user, String password, String schema) {
		this.url = url;
		this.driver = driver;
		this.user = user;
		this.password = password;
		this.schema = schema;
	}
	
}
