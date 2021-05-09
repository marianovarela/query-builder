package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAssociation extends Query{

	private Long leftId;
	
	private Long rightId;
	
	private Integer inverseType;
	
	public UpdateAssociation withLeftId(Long leftId) {
		this.leftId = leftId;
		return this;
	}
	
	public UpdateAssociation withRightId(Long rightId) {
		this.rightId = rightId;
		return this;
	}
	
	public UpdateAssociation withInverseType(Integer inverseType) {
		this.inverseType = inverseType;
		return this;
	}
	
}
