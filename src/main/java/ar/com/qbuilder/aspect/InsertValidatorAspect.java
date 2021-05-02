package ar.com.qbuilder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Query;
import ar.com.qbuilder.exception.BusinessException;
import ar.com.qbuilder.utils.MessageUtils;

@Aspect
@Component
public class InsertValidatorAspect {

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
    		if(insertion.getId() == null && insertion.getObject() == null) {
            	throw new BusinessException(MessageUtils.INSERT_PARAMETERS_ARE_INCORRECT);
            }
    	}else if(obj instanceof InsertAssociation){
    		InsertAssociation insertion = (InsertAssociation) joinPoint.getArgs()[0];
    		if(insertion.getLeftId() == null && insertion.getRightId() == null) {
            	throw new BusinessException(MessageUtils.INSERT_PARAMETERS_ARE_INCORRECT);
            }
    	}
    }
}
