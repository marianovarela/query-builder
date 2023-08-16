package io.github.marianovarela.qbuilder.domain;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeleteAssociation extends Delete{

	private Long leftId;
	
	private Long rightId;
	
	public DeleteAssociation(long leftId, long rightId, long type) {
		this.leftId = leftId;
		this.rightId = rightId;
		this.type = this.type;
	}

	@PostConstruct
	public void init() {
		this.table = "associations";
	}
	
}
