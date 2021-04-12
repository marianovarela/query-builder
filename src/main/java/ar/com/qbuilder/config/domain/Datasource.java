package ar.com.qbuilder.config.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Datasource {

	private String url;
	
	private String driver;
	
	private String user;
	
	private String password;
	
	public Datasource(String url, String driver, String user, String password) {
		this.url = url;
		this.driver = driver;
		this.user = user;
		this.password = password;
	}
	
}
