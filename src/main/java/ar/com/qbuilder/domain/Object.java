package ar.com.qbuilder.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Object implements Serializable{

	private static final long serialVersionUID = -7425917524951922564L;

	private long id;
	private int type;
	private String data;
	
}
