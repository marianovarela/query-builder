package ar.com.qbuilder.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AssociationKeys {

	// here are saved the types and their inverses if they exist
	public Map<Integer, Integer> keys = new HashMap<Integer, Integer>();
	
	public Integer getInverse(Integer type) {
		return this.keys.get(type);
	}
	
}
