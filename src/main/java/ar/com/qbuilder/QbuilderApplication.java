package ar.com.qbuilder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.domain.Condition;
import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.DeleteObject;
import ar.com.qbuilder.domain.Entity;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Join;
import ar.com.qbuilder.domain.JoinType;
import ar.com.qbuilder.domain.LogicOperator;
import ar.com.qbuilder.domain.Range;
import ar.com.qbuilder.domain.ResultSet;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.domain.TimeRange;
import ar.com.qbuilder.domain.UpdateAssociation;
import ar.com.qbuilder.domain.UpdateObject;
import ar.com.qbuilder.service.TestService;
import ar.com.qbuilder.service.executor.Executor;

@EnableAutoConfiguration
@SpringBootApplication
public class QbuilderApplication {

	@Autowired
	Executor executor;
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(QbuilderApplication.class, args);
		Config config = context.getBean(Config.class);
		TestService springBean = context.getBean(TestService.class);
				
		Executor executor = context.getBean(Executor.class);
		
		Map<Integer, Integer> keys = makeInitialKeys();
		executor.setKeys(keys);
//		InsertObject query = makeInsertObject();
//		UpdateObject query = makeUpdateObject();
//		UpdateAssociation query = makeUpdateAssociationWithInverse();
//		InsertAssociation query = makeInsertAssociationWithInverse();
//		InsertAssociation query = makeInsertAssociationWithoutInverse();
//		Selection query = new Selection("assocations");
//		DeleteAssociation query = makeDeleteAssociation();
//		DeleteObject query = makeDeleteObject();
//		SelectAssociation query = makeCountAssociation();
//		SelectAssociation query = makeTimeRangeAssociation();
//		SelectAssociation query = makeRangeAssociation();
//		SelectObject query = makeSelectObject();
//		SelectAssociation query = makeSelectAssociation();
		Join query = makeJoin();
//		SelectCustom query = makeSelectCustom();
		ResultSet result = executor.execute(query); 
		List<Row> rows = result.get();
		long count = result.count();
		String message = result.getMessage();
		boolean isOk = result.isStatus();
	}
	
	private static SelectCustom makeSelectCustom() {
		SelectCustom select = new SelectCustom();
		select.addToSelect("data", "cuerpo");
		select.addToSelect("id", "idx");
		select.addToSelect("type", "tipo");
		select.setEntity(Entity.Objects);
		select.setCondition("type = 3");
		
		return select;
	}

	private static Join makeJoin() {
		SelectCustom from = makeSelectCustomFrom();
		SelectCustom to = makeSelectCustomTo();
		Join join = new Join();
		join.setFrom(from);
		join.setTo(to);
		Condition condition = Condition.makeWithFirstTableAndSecondTable(LogicOperator.AND,"id", "id");
		Condition condition2 = Condition.makeWithFirstTableAndValue(LogicOperator.AND, "id", "30");
		Condition condition3 = Condition.makeWithSecondTableAndValue(LogicOperator.AND, "id", "30");
		join.getJoinClause().add(condition);
//		join.getJoinClause().add(condition2);
		join.setFilter("df1.id = 30");
		join.setType(JoinType.INNER);
		return join;
	}

	private static SelectCustom makeSelectCustomFrom() {
		SelectCustom query = new SelectCustom();
//		query.setAlias("df1");
		query.setEntity(Entity.Objects);
		query.setCondition("(type = 3)");
		
		return query;
	}
	
	private static SelectCustom makeSelectCustomTo() {
		SelectCustom query = new SelectCustom();
		query.setEntity(Entity.Objects);
		
		return query;
	}

	private static Map<Integer, Integer> makeInitialKeys() {
		Map<Integer, Integer> keys = new HashMap<Integer, Integer>();
		keys.put(1, 2);
		keys.put(2, 1);
		keys.put(4, 5);
		keys.put(5, 4);
		
		return keys;
	}

	private static SelectAssociation makeTimeRangeAssociation() {
		SelectAssociation query = new SelectAssociation();
		query.setLeftId(152L);
		query.setType(23);
		query.setLimit(1);
		TimeRange range = new TimeRange();
		range.setLow(10L);
		range.setHigh(15L);
		query.setTimeRange(range);
		return query;
	}
	
	private static SelectAssociation makeRangeAssociation() {
		SelectAssociation query = new SelectAssociation();
		query.setLeftId(152L);
		query.setType(23);
		Range range = new Range();
		range.setPosition(2);
		range.setLimit(4);
		query.setRange(range);
		return query;
	}

	private static UpdateAssociation makeUpdateAssociationWithInverse() {
		UpdateAssociation association = new UpdateAssociation();
		association.setLeftId(152L);
		association.setRightId(164L);
		association.setType(22);
		association.setTime(1L);
		association.setData("{field: 1}");
		return association;
	}

	private static UpdateAssociation makeUpdateAssociationWithoutInverse() {
		UpdateAssociation association = new UpdateAssociation();
		association.setLeftId(152L);
		association.setRightId(164L);
		association.setType(22);
		association.setData("{field: 1}");
		return association;
	}
	
	private static UpdateObject makeUpdateObject() {
		UpdateObject updation = new UpdateObject();
		updation.setId(100203L);
		updation.setType(10);
		updation.setData("'{'id': 'algun id'}'");
		return updation;
	}

	private static SelectAssociation makeSelectAssociation() {
		SelectAssociation query = new SelectAssociation();
		query.setLeftId(10L);
		query.setType(10);
		return query;
	}

	private static SelectObject makeSelectObject() {
		SelectObject select = new SelectObject();
		select.setId(3L);
		return select;
	}

	private static SelectAssociation makeCountAssociation() {
		SelectAssociation query = new SelectAssociation();
		query.setLeftId(152L);
		query.setRightId(153L);
		query.setType(10);
		return query;
	}

	private static DeleteObject makeDeleteObject() {
		DeleteObject delete = new DeleteObject();
		delete.setId(100203);
		return delete;
	}
	
	private static DeleteAssociation makeDeleteAssociation() {
		DeleteAssociation query = new DeleteAssociation();
		query.setLeftId(302L);
		query.setRightId(203L);
		query.setType(10);
		return query;
	}

	private static InsertAssociation makeInsertAssociationWithInverse() {
		InsertAssociation association = new InsertAssociation();
		association.withLeftId(152L);
		association.setRightId(203L);
		association.setType(10);
		association.setInverseType(20);
		association.setData("{}");
		association.setTable("associations");
		return association;
	}
	
	private static InsertAssociation makeInsertAssociationWithoutInverse() {
		InsertAssociation association = new InsertAssociation();
		association.withLeftId(312L);
		association.setRightId(203L);
		association.setType(10);
		association.setData("{}");
		return association;
	}

	private static InsertObject makeInsertObject() {
		InsertObject insertion = new InsertObject();
		insertion.setId(100203L);
		insertion.setType(10);
		insertion.setData("'{}'");
		return insertion;
	}
	
}
