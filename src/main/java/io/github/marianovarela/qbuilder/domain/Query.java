package io.github.marianovarela.qbuilder.domain;

import io.github.marianovarela.qbuilder.exception.BusinessException;
import io.github.marianovarela.qbuilder.utils.MessageUtils;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Query {
	
	protected String table;
	
	protected Long type;

	public Query() { throw new BusinessException(MessageUtils.CANNOT_INSTANTIATE); }
	
	public Query(String table) {
		this.table = table;
	}
	
}
