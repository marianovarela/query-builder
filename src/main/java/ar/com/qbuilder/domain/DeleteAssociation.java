package ar.com.qbuilder.domain;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeleteAssociation extends Delete{

	private Long leftId;
	
	private Long rightId;
	
	@PostConstruct
	public void init() {
		this.table = "associations";
	}
	
}
