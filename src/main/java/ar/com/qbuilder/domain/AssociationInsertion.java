package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociationInsertion extends Query{

	private Long leftId;
	
	private Long rightId;
	
	public AssociationInsertion withLeftId(Long leftId) {
		this.leftId = leftId;
		return this;
	}
	
	public AssociationInsertion withRightId(Long rightId) {
		this.rightId = rightId;
		return this;
	}
	
}
