package io.github.marianovarela.qbuilder.domain;

import io.github.marianovarela.qbuilder.exception.BusinessException;
import io.github.marianovarela.qbuilder.utils.MessageUtils;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectObject extends Select{

	private Long id;
	
	private String table = "objects";

	public SelectObject() {
		throw new BusinessException(MessageUtils.CANNOT_INSTANTIATE);
	}
	
	public SelectObject(Long id) {
		this.id = id;
	}

}
