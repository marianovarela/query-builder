package io.github.marianovarela.qbuilder.domain;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class SelectAbstract {

	// string with columns separated with comma
		private Optional<GroupBy> groupBy;
		
		private Optional<OrderBy> orderBy;
		
		private Optional<String> having;
		
		private Optional<Where> where;
		
		protected SelectAbstract() {
			this.groupBy = Optional.empty();
			this.orderBy = Optional.empty();
			this.having = Optional.empty();
			this.where = Optional.empty();
		}

}
