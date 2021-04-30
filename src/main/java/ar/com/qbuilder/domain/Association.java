package ar.com.qbuilder.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Association implements Serializable{

	private static final long serialVersionUID = -7425917524951922564L;

	private long left_id;
	private long right_id;
	private long type;
	
}
