package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectAssociation extends Select{

	private Long leftId;
	
	private Long rightId;
	
	private String table = "associations";
	
	private Integer limit;
	
	private TimeRange timeRange;
	
	private Range range;
	
	public SelectAssociation(String table) {
		super(table);
	}

	public SelectAssociation() {
		super();
	}

}
