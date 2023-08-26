package io.github.marianovarela.qbuilder.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.plans.logical.SubqueryAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.config.Config;
import io.github.marianovarela.qbuilder.config.domain.Datasource;
import io.github.marianovarela.qbuilder.domain.AggregationColumn;
import io.github.marianovarela.qbuilder.domain.Condition;
import io.github.marianovarela.qbuilder.domain.Join;
import io.github.marianovarela.qbuilder.domain.LogicOperator;
import io.github.marianovarela.qbuilder.domain.Order;
import io.github.marianovarela.qbuilder.domain.OrderedColumn;
import io.github.marianovarela.qbuilder.domain.SelectCustom;
import io.github.marianovarela.qbuilder.domain.Subquery;
import io.github.marianovarela.qbuilder.domain.Union;
import io.github.marianovarela.qbuilder.exception.BusinessException;
import io.github.marianovarela.qbuilder.helper.TaoSelector;

@Service
public class SelectionCustomService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;
	
	@Autowired
	private ApplicationContext context;
	
	private Config config;
	
	public Dataset<Row> getDataset(Subquery subquery) {
		if(subquery.getSelect() == null) {
			return subquery.getResultSet().getDataset();
		} else {
			return this.getDataset(subquery.getSelect());
		}
	}
	
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
		
		if((!(select.getGroupBy().isEmpty())) && !(select.getGroupBy().get().getColumns() == null) && !(select.getGroupBy().get().getColumns() == null || select.getGroupBy().get().getColumns().trim().length() == 0)) {
			RelationalGroupedDataset groupedDataset	= result.groupBy(select.getGroupBy().get().getColumns());
			boolean isFirst = true;
			for(AggregationColumn agg : select.getGroupBy().get().getAggregations()) {
				if(isFirst) {
					result = groupedDataset.agg(makeColumn(agg, result));
					isFirst = false;
				} else {
					result = result.agg(makeColumn(agg, result));
				}
			}
			if(select.getHaving().isPresent() && !(select.getHaving().isEmpty() || select.getHaving().get().trim().length() == 0)) {
				result = result.filter(select.getHaving().get());
			}
		}
		
		if((!(select.getOrderBy().isEmpty())) && !select.getOrderBy().get().isEmpty()) {
			result = orderBy(result, select);
		}

		return result;
	}
	
	private Dataset<Row> orderBy(Dataset<Row> result, SelectCustom select) {
			Column[] columns = makeOrderedColumns(result, select.getOrderBy().get().getColumns());
			result = result.orderBy(columns);
		return result;
	}
	
	private Dataset<Row> orderBy(Dataset<Row> result, Union union) {
		Column[] columns = makeOrderedColumns(result, union.getOrderBy().get().getColumns());
		result = result.orderBy(columns);
	return result;
}
	
	private Dataset<Row> orderBy(Dataset<Row> result, Join join) {
		Column[] columns = makeOrderedColumns(result, join.getOrderBy().get().getColumns());
		result = result.orderBy(columns);
	return result;
}

	private Column[] makeOrderedColumns(Dataset<Row> result, List<OrderedColumn> list) {
		Column[] columns = new Column[list.size()];
		int index = 0;
		for(io.github.marianovarela.qbuilder.domain.OrderedColumn column : list) {
			if(column.getOrder().equals(Order.ASC)) {
				columns[index] = result.col(column.getColumn()).asc();
			} else { // ORDER.DESC
				columns[index] = result.col(column.getColumn()).desc();
			}
			index++;
		}
		return columns;
	}

	public Dataset<Row> execute(SelectCustom select) {
		Dataset<Row> dataset = this.getDataset(select);
		return dataset;
	}

	private Column[] makeColumns(Dataset<Row> dataset, List<io.github.marianovarela.qbuilder.domain.Column> list) {
		Column[] columns = new Column[list.size()];
		int index = 0;
		for(io.github.marianovarela.qbuilder.domain.Column column : list) {
			if(column instanceof io.github.marianovarela.qbuilder.domain.AggregationColumn) {
				AggregationColumn aggCol = (AggregationColumn) column;
				columns[index] = makeColumn(aggCol, dataset);
			} else { // ar.com.qbuilder.domain.Column
				columns[index] = makeColumn(column, dataset);
			}
			index++;
		}
		return columns;
	}

	private Column makeColumn(io.github.marianovarela.qbuilder.domain.Column column, Dataset<Row> dataset) {
		if(column.getAlias().isEmpty()) {
			return dataset.col(String.valueOf(column.getColumn()));
		}
		return dataset.col(String.valueOf(column.getColumn())).alias(column.getAlias().get());
	}
	
	private Column makeColumn(AggregationColumn column, Dataset<Row> dataset) {
		//the column must have alias
		return getAggregateColumn(column, dataset).alias(column.getAlias().get());
	}
	
	private Column getAggregateColumn(AggregationColumn column, Dataset<Row> dataset) {
		int function = column.getAggregation().ordinal();
		Column tmpCol = dataset.col(String.valueOf(column.getColumn()));
		return getAggregatedFunction(function, tmpCol);
	}

	private Column getAggregatedFunction(int function, Column tmpCol) {
		switch(function) {
			case 0: //count
				return functions.count(tmpCol); 
			case 1: //max 
				return functions.max(tmpCol);
			case 2://min
				return functions.min(tmpCol);
			case 3://sum
				return functions.sum(tmpCol);
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
		if(join.getWhere().isPresent() && join.getWhere().get().getFilter() != null) {
			result = from.join(to, condition, join.getType().value)
					.filter(join.getWhere().get().getFilter());
		}
		result = addSelect(result, join);
		
		if((!(join.getGroupBy().isEmpty())) && !(join.getGroupBy().get().getColumns() == null) && !(join.getGroupBy().get().getColumns() == null || join.getGroupBy().get().getColumns().trim().length() == 0)) {
			RelationalGroupedDataset groupedDataset	= result.groupBy(join.getGroupBy().get().getColumns());
			boolean isFirst = true;
			for(AggregationColumn agg : join.getGroupBy().get().getAggregations()) {
				if(isFirst) {
					result = groupedDataset.agg(makeColumn(agg, result));
					isFirst = false;
				} else {
					result = result.agg(makeColumn(agg, result));
				}
			}
			if(!(join.getHaving().isEmpty() || join.getHaving().get().trim().length() == 0)) {
				result = result.filter(join.getHaving().get());
			}
		}
		
		if((!(join.getOrderBy().isEmpty())) && !join.getOrderBy().get().isEmpty()) {
			result = orderBy(result, join);
		}
		
		return result;
	}
	
	private Dataset<Row> addSelect(Dataset<Row> result, Join join) {
		if(join.getSelection().isPresent()) {
			result = result.select(join.getSelection().get());
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

	public Dataset<Row> execute(Union union) {
		Dataset<Row> first = this.getDataset(union.getFirst());
		first = first.alias("df1");
		Dataset<Row> second = this.getDataset(union.getSecond());
		second = second.alias("df2");
//		Dataset<Row> result = from.join(to, condition, join.getType().value);
		Dataset<Row> result = first.union(second);
		if(union.getWhere().isPresent() && union.getWhere().get().getFilter() != null) {
			result = first.union(second)
					.filter(union.getWhere().get().getFilter());
		}
		result = addSelect(result, union);
		
		if((!(union.getGroupBy().isEmpty())) && !(union.getGroupBy().get().getColumns() == null) && !(union.getGroupBy().get().getColumns() == null || union.getGroupBy().get().getColumns().trim().length() == 0)) {
			RelationalGroupedDataset groupedDataset	= result.groupBy(union.getGroupBy().get().getColumns());
			boolean isFirst = true;
			for(AggregationColumn agg : union.getGroupBy().get().getAggregations()) {
				if(isFirst) {
					result = groupedDataset.agg(makeColumn(agg, result));
					isFirst = false;
				} else {
					result = result.agg(makeColumn(agg, result));
				}
			}
			if(!(union.getHaving().isEmpty() || union.getHaving().get().trim().length() == 0)) {
				result = result.filter(union.getHaving().get());
			}
		}
		
		if((!(union.getOrderBy().isEmpty())) && !union.getOrderBy().get().isEmpty()) {
			result = orderBy(result, union);
		}
		
		return result;
	}

	private Dataset<Row> addSelect(Dataset<Row> result, Union union) {
		if(union.getSelection().isPresent()) {
			result = result.select(union.getSelection().get());
		}
		return result;
	}

}