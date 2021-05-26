package ar.com.qbuilder.service;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Condition;
import ar.com.qbuilder.domain.ConditionSimple;
import ar.com.qbuilder.domain.ConditionTree;
import ar.com.qbuilder.domain.SelectCustom;
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

	public Object execute(SelectCustom select) {
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
		return result.collect();
	}

	private String getStr(Condition element, boolean isFirst) {
		if (element instanceof ConditionSimple) {
			ConditionSimple simpleCondition = (ConditionSimple) element;
			if (isFirst) {
				return "condition";
			} else {
				return simpleCondition.getLogicOperator().toString() + " condition ";
			}
		} else if (element instanceof ConditionTree) {
			ConditionTree conditionTree = (ConditionTree) element;
			String partialRes = "";
			conditionTree.getConditions().stream().forEach(elem -> getStr(elem, true));
			int index = 0;
			boolean isFirstPartial = true;
			for(Condition condition : conditionTree.getConditions()) {
				partialRes += getStr(condition, isFirstPartial);
				if(index == 0) {
					isFirstPartial = false;
				}
			}
			if (isFirstPartial) {
				return "(" + partialRes + ")";
			} else {
				return" " + conditionTree.getLogicOperator().toString() + "(" + partialRes + ")"; 
			}
		}
		return null;
	}
	
	@PostConstruct
	private void postConstruct() {
		this.config = context.getBean(Config.class);
	}

}