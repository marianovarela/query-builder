package ar.com.qbuilder.aspect;

import java.util.List;

import org.apache.spark.sql.Row;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.UpdateAssociation;
import ar.com.qbuilder.exception.BusinessException;
import ar.com.qbuilder.helper.TaoSelector;
import ar.com.qbuilder.service.SparkService;
import ar.com.qbuilder.utils.MessageUtils;

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
    	SelectAssociation select = new SelectAssociation();
    	select.setLeftId(update.getLeftId());
    	select.setRightId(update.getRightId());
    	select.setType(update.getType());
    	
    	long indexTao = taoSelector.selectTao(select.getLeftId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		Row[] result = (Row[]) sparkService.execute(datasource, select);
    	if(result.length == 0) {
    		throw new BusinessException(MessageUtils.ASSOCIATION_DOES_NOT_EXIST);
    	}
    }
}
