package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertAssociation extends Query{

	private Long leftId;
	
	private Long rightId;
	
	private Long inverseType;
	
	private String data;
	
	private static String table = "associations";
	
	public InsertAssociation(Long leftId, Long rightId) {
		this.leftId = leftId;
		this.rightId = rightId;
	}
	
	public InsertAssociation withInverseType(Long inverseType) {
		this.inverseType = inverseType;
		return this;
	}
	
}
