package ar.com.qbuilder.helper;

import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.TimeRange;

@Service
public class FilterBuilder {
	
	public String addFilter(String field, String value, String filter) {
		if(filter.isEmpty()) {
			return field + " = " + value;  
		} else {
			return filter + " and " + field + " = " + value; 
		}
	}

	public String addTimeRange(String field, TimeRange timeRange, String filter) {
		String condition = field + " BETWEEN '" + timeRange.getLow().toString() + "' AND '" + timeRange.getHigh().toString() + "'";
		if(filter.isEmpty()) {
			return condition;  
		} else {
			return filter + " and " + condition; 
		}
	}
	
}
