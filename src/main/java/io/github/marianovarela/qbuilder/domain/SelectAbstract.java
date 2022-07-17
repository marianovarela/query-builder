package io.github.marianovarela.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class SelectAbstract {

	// string with columns separated with comma
		private GroupBy groupBy;
		
		private OrderBy orderBy;
		
		private String having;
		
		private Where where;
	
}
