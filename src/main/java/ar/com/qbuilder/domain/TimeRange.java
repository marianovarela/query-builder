package ar.com.qbuilder.domain;


import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TimeRange {

	private Date low;
	
	private Date high;
}
