package ar.com.qbuilder.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.plans.logical.SubqueryAlias;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Condition;
import ar.com.qbuilder.domain.Join;
import ar.com.qbuilder.domain.LogicOperator;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.helper.ResultBuilder;
import ar.com.qbuilder.helper.TaoSelector;
import lombok.extern.slf4j.Slf4j;

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
		Dataset<Row> result = null;
//		if(select.getSelection().isEmpty()) { // no hay selects ni alias predefinido
			result = sparkService.getEmptyDataSet(select.getEntity());
//		} //else {
//			result = sparkService.getEmptyDataSet(select);
//		}
		
		for(int index : taos) {
			long indexTao = taoSelector.selectTao(index);
			Datasource datasource = taoSelector.getDatasource(indexTao);
			Dataset<Row> resultTao = sparkService.execute(datasource, select);
			
			result = result.union(resultTao);
		}
		if(!select.getSelection().isEmpty()) {
			Column[] columns = makeColumns(result, select.getSelection());
			result = result.select(columns);
		}
		return result;
	}
	
	public Dataset<Row> execute(SelectCustom select) {
		Dataset<Row> dataset = this.getDataset(select);
		return dataset;
	}

	private Column[] makeColumns(Dataset<Row> dataset, List<Pair<String, String>> selection) {
		Column[] columns = new Column[selection.size()];
		int index = 0;
		for(Pair<String, String> pair : selection) {
			if(pair.getValue1() == null) {
				columns[index] = dataset.col(String.valueOf(pair.getValue0()));				
			} else {
				columns[index] = dataset.col(String.valueOf(pair.getValue0())).alias(String.valueOf(pair.getValue1()));
			}
			index++;
		};
		return columns;
	}

	@PostConstruct
	private void postConstruct() {
		this.config = context.getBean(Config.class);
	}

	public Dataset<Row> execute(Join join) {
		Dataset<Row> from = this.getDataset(join.getFrom());
		Dataset<Row> to = this.getDataset(join.getTo());
		Column condition = makeJoinCondition(from , to, join);
		Dataset<Row> result = from.join(to, condition, join.getType().value);
		if(join.getWhere() != null) {
			result = from.join(to, condition, join.getType().value)
//					.select(from.col("id").alias("df1_id"))
//					.select("id as df1_id")
					.where(join.getWhere());
		}
		return result;
	}
	
	public static <T> Optional<String> getAlias(Dataset<T> dataset){
	    final LogicalPlan analyzed = dataset.queryExecution().analyzed();
	    if(analyzed instanceof SubqueryAlias) {
	        SubqueryAlias subqueryAlias = (SubqueryAlias) analyzed;
	        return Optional.of(subqueryAlias.alias());
	    }
	    return Optional.empty();
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
			if(condition.getSecondColumn() != null) {
				column = from.col(condition.getFirstColumn()).equalTo(to.col(condition.getSecondColumn()));
			}else {// value != null
				if(condition.getNulleable() == null) {
					column = from.col(condition.getFirstColumn()).equalTo(condition.getValue());
				} else {
					if(condition.getNulleable()) {
						column = to.col(condition.getFirstColumn()).isNull();
					}else {
						column = to.col(condition.getFirstColumn()).isNotNull();
					}
				}
			}
		} else {
			if(condition.getNulleable() == null) {
				column = to.col(condition.getSecondColumn()).equalTo(condition.getValue());
			} else {
				if(condition.getNulleable()) {
					column = to.col(condition.getSecondColumn()).isNull();
				}else {
					column = to.col(condition.getSecondColumn()).isNotNull();
				}
			}
		}
		return column;
	}

}