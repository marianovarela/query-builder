package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAssociation extends Query{

	private Long leftId;
	
	private Long rightId;
	
	private String data;
	
	private Long time;
	
	private static String table = "associations";
	
}
