package io.github.marianovarela.qbuilder;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import io.github.marianovarela.qbuilder.config.Config;
import io.github.marianovarela.qbuilder.domain.Aggregation;
import io.github.marianovarela.qbuilder.domain.AggregationColumn;
import io.github.marianovarela.qbuilder.domain.Condition;
import io.github.marianovarela.qbuilder.domain.DeleteAssociation;
import io.github.marianovarela.qbuilder.domain.DeleteObject;
import io.github.marianovarela.qbuilder.domain.Entity;
import io.github.marianovarela.qbuilder.domain.GroupBy;
import io.github.marianovarela.qbuilder.domain.InsertAssociation;
import io.github.marianovarela.qbuilder.domain.InsertObject;
import io.github.marianovarela.qbuilder.domain.Join;
import io.github.marianovarela.qbuilder.domain.JoinType;
import io.github.marianovarela.qbuilder.domain.LogicOperator;
import io.github.marianovarela.qbuilder.domain.Order;
import io.github.marianovarela.qbuilder.domain.OrderBy;
import io.github.marianovarela.qbuilder.domain.OrderedColumn;
import io.github.marianovarela.qbuilder.domain.Range;
import io.github.marianovarela.qbuilder.domain.ResultSet;
import io.github.marianovarela.qbuilder.domain.SelectAssociation;
import io.github.marianovarela.qbuilder.domain.SelectCustom;
import io.github.marianovarela.qbuilder.domain.SelectObject;
import io.github.marianovarela.qbuilder.domain.TimeRange;
import io.github.marianovarela.qbuilder.domain.Union;
import io.github.marianovarela.qbuilder.domain.UpdateAssociation;
import io.github.marianovarela.qbuilder.domain.UpdateObject;
import io.github.marianovarela.qbuilder.domain.Where;
import io.github.marianovarela.qbuilder.service.TestService;
import io.github.marianovarela.qbuilder.service.executor.Executor;

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
		UpdateObject query = makeUpdateObject();
//		UpdateAssociation query = makeUpdateAssociationWithInverse();
//		InsertAssociation query = makeInsertAssociationWithInverse();
//		InsertAssociation query = makeInsertAssociationWithoutInverse();
		
//		DeleteAssociation query = makeDeleteAssociation();
//		DeleteObject query = makeDeleteObject();
//		SelectAssociation query = makeCountAssociation();
//		SelectAssociation query = makeTimeRangeAssociation();
//		SelectAssociation query = makeRangeAssociation();
//		SelectObject query = makeSelectObject();
//		SelectAssociation query = makeSelectAssociation();
//		Join query = makeJoin();
//		Union query = makeUnion();
//		SelectCustom query = makeSelectCustom();
//		SelectCustom query = makeGroupByHaving();
//		SelectCustom query = makeOrderBy();
		Date startDate = new Date();
				
		ResultSet result = executor.execute(query); 
		if(result.isError()) {
			String message = result.getMessage();
		} else {
//			List<Row> rows = result.get();
			Date endDate   = new Date();
//			long count = result.count();
			
			long duration  = endDate.getTime() - startDate.getTime();
			System.out.println("diff " + duration);
			System.out.println("millis " + ((double) duration / 1000) );
		}
	}

	private static SelectCustom makeOrderBy() {
		SelectCustom select = new SelectCustom();
//		select.setEntity(Entity.Objects);
		select.setEntity(Entity.Associations);
//		select.addToSelect("type", null);
//		select.addToSelect("id", null);
		OrderBy orderBy = OrderBy.build()
				.addColumn(OrderedColumn.buildColumn("type", Order.DESC))
				.addColumn(OrderedColumn.buildColumn("left_id", Order.ASC));
		
		select.setOrderBy(orderBy);
		//select.setCondition("type = -2001");
		Where where = new Where("type = -2001");
		select.setWhere(where);
		return select;
	}

	private static SelectCustom makeGroupByHaving() {
		SelectCustom select = new SelectCustom();
//		select.setEntity(Entity.Objects);
		select.setEntity(Entity.Associations);
//		select.addToSelect("id", "sum", Aggregation.SUM);
		select.addToSelect("type", null);
//		select.addToSelect("type", "count", Aggregation.COUNT);
		GroupBy groupBy = GroupBy.build()
				.setGroupBy("type")
				.addAggregation(AggregationColumn.buildColumn("type", "count", Aggregation.COUNT));
		
		select.setGroupBy(groupBy);
		
//		select.setGroupBy("type");
		select.setHaving("count > 20");
		
		return select;
	}

	private static SelectCustom makeSelectCustom() {
		SelectCustom select = new SelectCustom();
//		select.addToSelect("data", "cuerpo");
//		select.addToSelect("id", "idx");
//		select.addToSelect("type", "tipo");
//		select.addToSelect("id", "count", Aggregation.COUNT);
//		select.setEntity(Entity.Objects);
		select.setEntity(Entity.Associations);
		Where where = new Where("type = -2001");
		select.setWhere(where);
		
		return select;
	}

	private static Join makeJoin() {
		SelectCustom from =  new SelectCustom();
//		query.setAlias("df1");
		from.setEntity(Entity.Associations);
		Where where = new Where("type = -2001");
		from.setWhere(where);
		//from.setCondition("(type = -2001)");
		SelectCustom to = new SelectCustom();
//		query.setAlias("df1");
		to.setEntity(Entity.Associations);
		Where where2 = new Where("type = -2001");
		to.setWhere(where2);
		//to.setCondition("(type = -2001)");
		Join join = new Join();
		join.withFrom(from);
		join.withTo(to);
		Condition condition2 = Condition.makeWithFirstTableAndValue(LogicOperator.AND, "left_id", "30");
		Condition condition3 = Condition.makeWithSecondTableAndValue(LogicOperator.AND, "left_id", "30");
		join.getJoinClause().add(condition2);
		join.getJoinClause().add(condition3);
//		join.setFilter("df1.id = 30");
//		join.setSelection("df1.id");
		join.setType(JoinType.INNER);
		return join;
	}
	
	private static Union makeUnion() {
		SelectCustom first =  new SelectCustom();
//		query.setAlias("df1");
		first.setEntity(Entity.Associations);
		Where where = new Where("type = -2001");
		first.setWhere(where);
//		first.setCondition("(type = -2001)");
		SelectCustom second = new SelectCustom();
//		query.setAlias("df1");
		second.setEntity(Entity.Objects);
//		second.setCondition("(type = -4001)");
		Where where2 = new Where("type = -4001");
		second.setWhere(where2);
		Union union = new Union();
		union.withFirst(first).withSecond(second);
//		union.setFilter("df1.id = 30");
//		union.setSelection("df1.id");
		return union;
	}

	private static SelectCustom makeSelectCustomFrom() {
		SelectCustom query = new SelectCustom();
//		query.setAlias("df1");
		query.setEntity(Entity.Objects);
		Where where = new Where("type = -2001");
		query.setWhere(where);
//		query.setCondition("(type = 3)");
		
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
		query.setLeftId(152L);
//		query.setType(10);
		return query;
	}

	private static SelectObject makeSelectObject() {
		SelectObject select = new SelectObject();
		select.setId(1L);
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
		delete.setId(100233);
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
