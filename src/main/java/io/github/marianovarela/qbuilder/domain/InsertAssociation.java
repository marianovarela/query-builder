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
	
	private String table = "associations";
	
	public InsertAssociation(Long leftId, Long rightId, Long type) {
		this.leftId = leftId;
		this.rightId = rightId;
		this.type = type;
		this.table = "associations";
	}
	
	public InsertAssociation withInverseType(Long inverseType) {
		this.inverseType = inverseType;
		return this;
	}
	
}
