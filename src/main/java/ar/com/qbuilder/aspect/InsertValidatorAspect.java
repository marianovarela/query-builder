package ar.com.qbuilder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.domain.ObjectInsertion;
import ar.com.qbuilder.exception.BusinessException;
import ar.com.qbuilder.utils.MessageUtils;

@Aspect
@Component
public class InsertValidatorAspect {

    @Before(value = "@annotation(ar.com.qbuilder.aspect.InsertValidator)")
    public void getInsertionParameters(JoinPoint joinPoint) {        
        ObjectInsertion insertion = (ObjectInsertion) joinPoint.getArgs()[0];
        /*if(insertion.getTable() == null) {
        	throw new BusinessException(MessageUtils.TABLE_MUST_BE_SPECIFIED);
        }
        if(insertion.getType() == null) {
        	throw new BusinessException(MessageUtils.TYPE_MUST_BE_SPECIFIED);
        }
        if(insertion.getId() != null && insertion.getObject() != null && 
        		(insertion.getLeftId() != null || insertion.getRightId() != null)) {
        	throw new BusinessException(MessageUtils.INSERT_PARAMETERS_ARE_INCORRECT);
        }
        if(insertion.getLeftId() != null && insertion.getRightId() != null && 
        		(insertion.getObject() != null || insertion.getId() != null)) {
        	throw new BusinessException(MessageUtils.INSERT_PARAMETERS_ARE_INCORRECT);
        }*/
    }
}
