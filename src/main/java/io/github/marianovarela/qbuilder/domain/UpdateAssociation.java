package io.github.marianovarela.qbuilder.domain;

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
	
	public UpdateAssociation(Long leftId, Long rightId, Long type) {
		this.leftId = leftId;
		this.rightId = rightId;
		this.type = type;
	}
	
}
