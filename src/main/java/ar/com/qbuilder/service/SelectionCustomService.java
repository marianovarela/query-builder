package ar.com.qbuilder.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.plans.logical.SubqueryAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.AggregationColumn;
import ar.com.qbuilder.domain.Condition;
import ar.com.qbuilder.domain.Join;
import ar.com.qbuilder.domain.LogicOperator;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.exception.BusinessException;
import ar.com.qbuilder.helper.TaoSelector;

@Service
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
		result = sparkService.getEmptyDataSet(select.getEntity());
		
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
		
		if((!(select.getGroupBy() == null)) && !select.getGroupBy().isBlank()) {
			//TODO: pensar como hacer para pasar la agregación y la funcion count
			result = result.groupBy(select.getGroupBy())
				.agg(functions.count(result.col("type")).alias("count"));
			if(!select.getHaving().isBlank()) {
				result = result.filter(select.getHaving());
			}
		}

		return result;
	}
	
	public Dataset<Row> execute(SelectCustom select) {
		Dataset<Row> dataset = this.getDataset(select);
		return dataset;
	}

	private Column[] makeColumns(Dataset<Row> dataset, List<ar.com.qbuilder.domain.Column> list) {
		Column[] columns = new Column[list.size()];
		int index = 0;
		for(ar.com.qbuilder.domain.Column column : list) {
			if(column instanceof ar.com.qbuilder.domain.AggregationColumn) {
				AggregationColumn aggCol = (AggregationColumn) column;
				columns[index] = makeColumn(aggCol, dataset);
			} else { // ar.com.qbuilder.domain.Column
				columns[index] = makeColumn(column, dataset);
			}
			index++;
		}
		/*for(Pair<String, String> pair : selection) {
			if(pair.getValue1() == null) {
				columns[index] = dataset.col(String.valueOf(pair.getValue0()));				
			} else {
				columns[index] = dataset.col(String.valueOf(pair.getValue0())).alias(String.valueOf(pair.getValue1()));
			}
			index++;
		};*/
		return columns;
	}

	private Column makeColumn(ar.com.qbuilder.domain.Column column, Dataset<Row> dataset) {
		if(column.getAlias() == null) {
			return dataset.col(String.valueOf(column.getColumn()));
		}
		return dataset.col(String.valueOf(column.getColumn())).alias(column.getAlias());
	}
	
	private Column makeColumn(AggregationColumn column, Dataset<Row> dataset) {
		//the column must have alias
		return getAggregateColumn(column, dataset).alias(column.getAlias());
	}

	private Column getAggregateColumn(AggregationColumn column, Dataset<Row> dataset) {
		int function = column.getAggregation().ordinal();
		switch(function) {
			case 0: //count
				return functions.count(dataset.col(String.valueOf(column.getColumn()))); 
			case 1: //max 
				return functions.max(dataset.col(String.valueOf(column.getColumn())));
			case 2://min
				return functions.min(dataset.col(String.valueOf(column.getColumn())));
			case 3://sum
				return functions.sum(dataset.col(String.valueOf(column.getColumn())));
			default: 
				throw new BusinessException("Function has not exist");
		}
	}

	@PostConstruct
	private void postConstruct() {
		this.config = context.getBean(Config.class);
	}

	public Dataset<Row> execute(Join join) {
		Dataset<Row> from = this.getDataset(join.getFrom());
		from = from.alias("df1");
		Dataset<Row> to = this.getDataset(join.getTo());
		to = to.alias("df2");
		Column condition = makeJoinCondition(from , to, join);
		Dataset<Row> result = from.join(to, condition, join.getType().value);
		if(join.getFilter() != null) {
			result = from.join(to, condition, join.getType().value)
					.filter(join.getFilter());
		}
		result = addSelect(result, join);
		return result;
	}
	
	private Dataset<Row> addSelect(Dataset<Row> result, Join join) {
		if(join.getSelection() != null) {
			result = result.select(join.getSelection());
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