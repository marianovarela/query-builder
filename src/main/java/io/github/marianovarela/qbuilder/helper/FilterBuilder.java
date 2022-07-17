package io.github.marianovarela.qbuilder.helper;

import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.domain.TimeRange;

@Service
public class FilterBuilder {
	
	public String addFilter(String field, String value, String accumulatedfilter) {
		if(accumulatedfilter.isEmpty()) {
			return field + " = " + value;  
		} else {
			return accumulatedfilter + " and " + field + " = " + value; 
		}
	}
	
	public String addFilter(String field, String value, String operator, String accumulatedfilter) {
		if(accumulatedfilter.isEmpty()) {
			return field + " = " + value;  
		} else {
			return accumulatedfilter + " " + operator + " " + field + " = " + value; 
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
