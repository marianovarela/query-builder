package io.github.marianovarela.qbuilder.aspect;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.marianovarela.qbuilder.config.domain.Datasource;
import io.github.marianovarela.qbuilder.domain.InsertAssociation;
import io.github.marianovarela.qbuilder.domain.InsertObject;
import io.github.marianovarela.qbuilder.domain.Query;
import io.github.marianovarela.qbuilder.domain.SelectAssociation;
import io.github.marianovarela.qbuilder.exception.BusinessException;
import io.github.marianovarela.qbuilder.helper.AssociationKeys;
import io.github.marianovarela.qbuilder.helper.TaoSelector;
import io.github.marianovarela.qbuilder.service.SparkService;
import io.github.marianovarela.qbuilder.utils.MessageUtils;

@Aspect
@Component
public class InsertValidatorAspect {
	
	@Autowired
	private AssociationKeys associationKeys;
	
	@Autowired
	private SparkService sparkService;

	@Autowired
	private TaoSelector taoSelector;

    @Before(value = "@annotation(ar.com.qbuilder.aspect.InsertValidator)")
    public void getInsertionParameters(JoinPoint joinPoint) { 
    	Query obj = (Query) joinPoint.getArgs()[0];
    	if(obj.getTable() == null) {
        	throw new BusinessException(MessageUtils.TABLE_MUST_BE_SPECIFIED);
        }
        if(obj.getType() == null) {
        	throw new BusinessException(MessageUtils.TYPE_MUST_BE_SPECIFIED);
        }
    	
        if(obj instanceof InsertObject) {
    		InsertObject insertion = (InsertObject) joinPoint.getArgs()[0];
    		if(insertion.getId() == null && insertion.getData() == null) {
            	throw new BusinessException(MessageUtils.INSERT_PARAMETERS_ARE_INCORRECT);
            }
    	}else if(obj instanceof InsertAssociation){
    		InsertAssociation insertion = (InsertAssociation) joinPoint.getArgs()[0];
    		if(insertion.getLeftId() == null && insertion.getRightId() == null) {
            	throw new BusinessException(MessageUtils.INSERT_PARAMETERS_ARE_INCORRECT);
            }
    		this.validAssociationKey(insertion.getType(), insertion.getInverseType());
    		this.existAssociation(insertion.getLeftId(), insertion.getRightId(), insertion.getType(), insertion.getInverseType());
    	}
    }
    
    private void existAssociation(Long leftId, Long rightId, Long type, Long inverseType) {
    	Dataset<Row> result = this.getAssociation(leftId, rightId, type, inverseType);
    	Row[] rows = (Row[]) result.collect();
    	if(rows.length > 0) {
    		throw new BusinessException(MessageUtils.ASSOCIATION_ALREADY_EXIST);
    	}	
    	if(inverseType != null) {
    		Dataset<Row> inverseResult = this.getAssociation(leftId, rightId, type, inverseType);
    		Row[] inverseRows = (Row[]) inverseResult.collect();
        	if(inverseRows.length > 0) {
        		throw new BusinessException(MessageUtils.ASSOCIATION_ALREADY_EXIST);
        	}	
    	}
	}
     
	private Dataset<Row> getAssociation(Long leftId, Long rightId, Long type, Long inverseType) {
		SelectAssociation select = new SelectAssociation(leftId);
    	select.setRightId(rightId);
    	select.setType(type);
    	
    	long indexTao = taoSelector.selectTao(select.getLeftId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		Dataset<Row> result = sparkService.execute(datasource, select);
		return result;
	}

	//valid if the association exists and if it exists valid if it is the same, if it is not the same it will throw exception.
  	private void validAssociationKey(Long type, Long inverseType) {
  		if(this.associationKeys.keys.containsKey(type)) {
  			Long retrievedInverseType = this.associationKeys.keys.get(type);
  			if(!inverseType.equals(retrievedInverseType)) {
  				throw new BusinessException(MessageUtils.INVERSE_TYPE_ALREADY_EXISTS_AND_ITS_DIFFERENT);
  			}
  		}
  	}
}
