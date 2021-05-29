package ar.com.qbuilder.domain;

public enum JoinType {
	
	INNER("inner");
	
	public final String value;

	JoinType(String value) {
		this.value = value;
	}
}
