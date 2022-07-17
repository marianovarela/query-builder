package ar.com.qbuilder.domain;

public enum JoinType {
	
	INNER("inner"),
	LEFT("left"),
	LEFT_OUTER("left_outer"),
	RIGHT("right"),
	RIGHT_OUTER("right_outer"),
	OUTER("outer");
	
	public final String value;

	JoinType(String value) {
		this.value = value;
	}
}
