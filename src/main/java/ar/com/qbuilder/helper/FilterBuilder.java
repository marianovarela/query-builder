package ar.com.qbuilder.helper;

import org.springframework.stereotype.Service;

@Service
public class FilterBuilder {
	
	public String addFilter(String condition, String value, String filter) {
		if(filter.isEmpty()) {
			return condition + " = " + value;  
		} else {
			return filter + " and " + condition + " = " + value; 
		}
	}
	
}
