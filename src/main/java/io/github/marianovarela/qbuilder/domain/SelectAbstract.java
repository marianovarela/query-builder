package io.github.marianovarela.qbuilder.domain;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter 
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

		public void setGroupBy(GroupBy groupBy) {
			Optional<GroupBy> optGroupBy = Optional.ofNullable(groupBy);
			this.groupBy = optGroupBy;
		}

		public void setOrderBy(OrderBy orderBy) {
			Optional<OrderBy> optOrderBy = Optional.ofNullable(orderBy);
			this.orderBy = optOrderBy;
		}

		public void setHaving(String having) {
			Optional<String> optHaving = Optional.ofNullable(having);
			this.having = optHaving;
		}

		public void setWhere(Where where) {
			Optional<Where> optWhere = Optional.ofNullable(where);
			this.where = optWhere;
		}
		
		

}
