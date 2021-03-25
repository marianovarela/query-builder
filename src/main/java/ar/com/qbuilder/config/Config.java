package ar.com.qbuilder.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("tao")
@Getter
@Setter
@AllArgsConstructor
public class Config {

	private long arity;

	public long getArity() {
		return arity;
	}

	public void setArity(long arity) {
		this.arity = arity;
	}

}