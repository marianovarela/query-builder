package ar.com.qbuilder;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Range;
import ar.com.qbuilder.domain.Select;
import ar.com.qbuilder.domain.SelectAssociation;
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
		UpdateAssociation query = makeUpdateAssociationWithInverse();
//		InsertAssociation query = makeInsertAssociationWithInverse();
//		InsertAssociation query = makeInsertAssociationWithoutInverse();
//		Selection query = new Selection("assocations");
//		DeleteAssociation query = makeDeleteAssociation();
//		SelectAssociation query = makeCountAssociation();
//		SelectAssociation query = makeTimeRangeAssociation();
//		SelectAssociation query = makeRangeAssociation();
//		SelectObject query = makeSelectObject();
//		SelectAssociation query = makeSelectAssociation();
//		Object result = executor.execute(query);
//		System.out.println(result);
		executor.execute(query); 
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
		range.setLow(Date.valueOf("2020-01-05"));
		range.setHigh(Date.valueOf("2022-01-05"));
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
		association.setNewType(23);
		association.setTable("associations");
		return association;
	}

	private static UpdateAssociation makeUpdateAssociationWithoutInverse() {
		UpdateAssociation association = new UpdateAssociation();
		association.setLeftId(152L);
		association.setRightId(164L);
		association.setType(22);
		association.setNewType(23);
		association.setTable("associations");
		return association;
	}
	
	private static UpdateObject makeUpdateObject() {
		UpdateObject updation = new UpdateObject();
		updation.setId(100203L);
		updation.setTable("objects");
		updation.setType(10);
		updation.setData("'{'id': 'algun id'}'");
		return updation;
	}

	private static SelectAssociation makeSelectAssociation() {
		SelectAssociation query = new SelectAssociation();
		query.setLeftId(152L);
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
		query.withCount();
		return query;
	}

	private static DeleteAssociation makeDeleteAssociation() {
		DeleteAssociation query = new DeleteAssociation();
		query.setLeftId(152L);
		query.setRightId(153L);
		query.setType(10);
		return query;
	}

	private static InsertAssociation makeInsertAssociationWithInverse() {
		InsertAssociation association = new InsertAssociation();
		association.withLeftId(152L);
		association.setRightId(203L);
		association.setType(10);
		association.setInverseType(20);
		association.setTable("associations");
		return association;
	}
	
	private static InsertAssociation makeInsertAssociationWithoutInverse() {
		InsertAssociation association = new InsertAssociation();
		association.withLeftId(312L);
		association.setRightId(203L);
		association.setType(10);
		association.setTable("associations");
		return association;
	}

	private static InsertObject makeInsertObject() {
		InsertObject insertion = new InsertObject();
		insertion.setId(100203L);
		insertion.setTable("objects");
		insertion.setType(10);
		insertion.setData("'{}'");
		return insertion;
	}

}
