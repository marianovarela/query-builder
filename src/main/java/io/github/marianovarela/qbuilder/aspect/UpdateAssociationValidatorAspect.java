package io.github.marianovarela.qbuilder.aspect;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.marianovarela.qbuilder.config.domain.Datasource;
import io.github.marianovarela.qbuilder.domain.SelectAssociation;
import io.github.marianovarela.qbuilder.domain.UpdateAssociation;
import io.github.marianovarela.qbuilder.exception.BusinessException;
import io.github.marianovarela.qbuilder.helper.TaoSelector;
import io.github.marianovarela.qbuilder.service.SparkService;
import io.github.marianovarela.qbuilder.utils.MessageUtils;

@Aspect
@Component
public class UpdateAssociationValidatorAspect {
	
	@Autowired
	private SparkService sparkService;

	@Autowired
	private TaoSelector taoSelector;
	
    @Before(value = "@annotation(ar.com.qbuilder.aspect.UpdateAssociationValidator)")
    public void getInsertionParameters(JoinPoint joinPoint) { 
    	UpdateAssociation update = (UpdateAssociation) joinPoint.getArgs()[0];
    	SelectAssociation select = new SelectAssociation(update.getLeftId());
    	select.setRightId(update.getRightId());
    	select.setType(update.getType());
    	
    	long indexTao = taoSelector.selectTao(select.getLeftId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		Dataset<Row> result = sparkService.execute(datasource, select);
		Row[] rows = (Row[]) result.collect();
    	if(rows.length == 0) {
    		throw new BusinessException(MessageUtils.ASSOCIATION_DOES_NOT_EXIST);
    	}
    }
}
