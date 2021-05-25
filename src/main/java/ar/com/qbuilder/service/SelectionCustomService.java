package ar.com.qbuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.Condition;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.domain.ConditionSimple;
import ar.com.qbuilder.domain.ConditionTree;
import ar.com.qbuilder.helper.TaoSelector;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SelectionCustomService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;

	public Object execute(SelectCustom query) {
		String res = "";
		boolean isFirst = true;
		int index = 0;
		for(Condition condition : query.getCondition()) {
			res += getStr(condition, isFirst);
			if(index == 0) {
				isFirst = false;
			}
		}
		log.info(res);
		return null;
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

}