package ar.com.qbuilder.service;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.aggregate.First;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Condition;
import ar.com.qbuilder.domain.Join;
import ar.com.qbuilder.domain.LogicOperator;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.helper.TaoSelector;
import lombok.extern.slf4j.Slf4j;
import shaded.parquet.org.apache.thrift.server.THsHaServer;

@Service
@Slf4j
public class SelectionCustomService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;
	
	@Autowired
	private ApplicationContext context;
	
	private Config config;

	public Dataset<Row> getDataset(SelectCustom select) {
		int arity = config.getArity();
		int[] taos = new int[arity];
		Arrays.setAll(taos, i -> i);
		Dataset<Row> result = sparkService.getEmptyDataSet();
		for(int index : taos) {
			long indexTao = taoSelector.selectTao(index);
			Datasource datasource = taoSelector.getDatasource(indexTao);
			Dataset<Row> resultTao = sparkService.execute(datasource, select);
			result = result.union(resultTao);
		}
		return result;
	}
	
	public Object execute(SelectCustom select) {
		Dataset<Row> dataset = this.getDataset(select);
		return dataset.collect();
	}

	@PostConstruct
	private void postConstruct() {
		this.config = context.getBean(Config.class);
	}

	public Row[] execute(Join join) {
		Dataset<Row> from = this.getDataset(join.getFrom());
		Dataset<Row> to = this.getDataset(join.getTo());
		Column condition = makeJoinCondition(from , to, join);//from.col("id").equalTo(to.col("id"));
		Dataset<Row> result = from.join(to, condition, join.getType().value);
				
		return (Row[]) result.collect();
	}

	private Column makeJoinCondition(Dataset<Row> from, Dataset<Row> to, Join join) {
		Condition condition = join.getJoinClause().get(0);
		Column column = makeColumn(from, to, condition);
		for (int i = 1; i < join.getJoinClause().size(); i++) {
			Condition otherCondition = join.getJoinClause().get(i);
			Column newColumn = makeColumn(from, to, otherCondition);
			if(otherCondition.getLogicOperator().equals(LogicOperator.AND)) {
				column.and(newColumn);
			} else { // or
				column.or(newColumn);
			}
		}
		return column;
	}

	// TODO: validar que las condiciones sean correctas
	private Column makeColumn(Dataset<Row> from, Dataset<Row> to, Condition condition) {
		Column column = null;
		if(condition.getFirstColumn() != null) {
			;
			if(condition.getSecondColumn() != null) {
				column = from.col(condition.getFirstColumn()).equalTo(to.col(condition.getSecondColumn()));
			}else {// value != null
				column = from.col(condition.getFirstColumn()).equalTo(condition.getValue());
			}
		} else {
			column = to.col(condition.getSecondColumn()).equalTo(condition.getValue());
		}
		return column;
	}

}