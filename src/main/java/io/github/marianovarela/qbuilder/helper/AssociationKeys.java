package io.github.marianovarela.qbuilder.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AssociationKeys {

	// here are saved the types and their inverses if they exist
	public Map<Long, Long> keys = new HashMap<Long, Long>();
	
	public Long getInverse(Long type) {
		return this.keys.get(type);
	}
	
}
