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
	
	public InsertAssociation withLeftId(Long leftId) {
		this.leftId = leftId;
		return this;
	}
	
	public InsertAssociation withRightId(Long rightId) {
		this.rightId = rightId;
		return this;
	}
	
	public InsertAssociation withInverseType(Long inverseType) {
		this.inverseType = inverseType;
		return this;
	}
	
}
