package ar.com.qbuilder.domain;

public enum Aggregation {

	COUNT("count"),
	MAX("max"),
	MIN("min"),
	SUM("sum");
	
	public final String value;

	Aggregation(String value) {
		this.value = value;
	}
	
}
