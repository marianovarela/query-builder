package ar.com.qbuilder.aspect;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Query;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.exception.BusinessException;
import ar.com.qbuilder.helper.AssociationKeys;
import ar.com.qbuilder.helper.TaoSelector;
import ar.com.qbuilder.service.SparkService;
import ar.com.qbuilder.utils.MessageUtils;

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
    
    private void existAssociation(Long leftId, Long rightId, Integer type, Integer inverseType) {
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
     
	private Dataset<Row> getAssociation(Long leftId, Long rightId, Integer type, Integer inverseType) {
		SelectAssociation select = new SelectAssociation();
    	select.setLeftId(leftId);
    	select.setRightId(rightId);
    	select.setType(type);
    	
    	long indexTao = taoSelector.selectTao(select.getLeftId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		Dataset<Row> result = sparkService.execute(datasource, select);
		return result;
	}

	//valid if the association exists and if it exists valid if it is the same, if it is not the same it will throw exception.
  	private void validAssociationKey(Integer type, Integer inverseType) {
  		if(this.associationKeys.keys.containsKey(type)) {
  			Integer retrievedInverseType = this.associationKeys.keys.get(type);
  			if(!inverseType.equals(retrievedInverseType)) {
  				throw new BusinessException(MessageUtils.INVERSE_TYPE_ALREADY_EXISTS_AND_ITS_DIFFERENT);
  			}
  		}
  	}
}
