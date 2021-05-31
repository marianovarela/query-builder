package ar.com.qbuilder.domain;

import lombok.Getter;
import lombok.Setter;
import scala.annotation.meta.setter;

@Getter @Setter
public class Condition {

	private LogicOperator logicOperator;
	
	//column of table a
	private String firstColumn;
	
	//column of table b
	private String secondColumn;
	
	private String value;
	
	private Boolean nulleable;

	public static Condition makeWithFirstTableAndSecondTable(LogicOperator logicOperator, String firstColumn, String secondColumn) {
		Condition condition = new Condition();
		condition.setLogicOperator(logicOperator);
		condition.setFirstColumn(firstColumn);
		condition.setSecondColumn(secondColumn);
		return condition;
	}

	public static Condition makeWithFirstTableAndValue(LogicOperator logicOperator, String firstColumn, String value) {
		Condition condition = new Condition();
		condition.setLogicOperator(logicOperator);
		condition.setFirstColumn(firstColumn);
		condition.setValue(value);
		return condition;
	}

	public static Condition makeWithFirstTableIsNull(LogicOperator logicOperator, String firstColumn, boolean nulleable) {
		Condition condition = new Condition();
		condition.setLogicOperator(logicOperator);
		condition.setFirstColumn(firstColumn);
		condition.setNulleable(nulleable);
		return condition;
	}
	
	public static Condition makeWithSecondTableAndValue(LogicOperator logicOperator, String secondColumn, String value) {
		Condition condition = new Condition();
		condition.setLogicOperator(logicOperator);
		condition.setSecondColumn(secondColumn);
		condition.setValue(value);
		return condition;
	}
	
	public static Condition makeWithSecondTableIsNull(LogicOperator logicOperator, String secondColumn, boolean nulleable) {
		Condition condition = new Condition();
		condition.setLogicOperator(logicOperator);
		condition.setSecondColumn(secondColumn);
		condition.setNulleable(nulleable);
		return condition;
	}
}
