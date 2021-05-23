package ar.com.qbuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.DeleteObject;
import ar.com.qbuilder.domain.Result;
import ar.com.qbuilder.helper.AssociationKeys;
import ar.com.qbuilder.helper.ResultBuilder;
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class DeletionService {

	@Autowired
	TaoSelector taoSelector;
	
	@Autowired
	SparkService sparkService;
	
	@Autowired
	AssociationKeys associationKeys;

	public Result execute(DeleteAssociation delete) {
		long indexTao = taoSelector.selectTao(delete.getLeftId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		sparkService.delete(datasource, delete);
		if(associationKeys.getInverse(delete.getType()) != null) {
			long indexInverseTao = taoSelector.selectTao(delete.getRightId());
			Datasource inverseDatasource = taoSelector.getDatasource(indexInverseTao);
			DeleteAssociation inverse = getInverse(delete);
			sparkService.delete(inverseDatasource, inverse);
		}
		return ResultBuilder.buildSuccess();
	}

	private DeleteAssociation getInverse(DeleteAssociation delete) {
		DeleteAssociation inverse = new DeleteAssociation();
		inverse.setLeftId(delete.getRightId());
		inverse.setTable(delete.getTable());
		inverse.setRightId(delete.getLeftId());
		inverse.setType(associationKeys.getInverse(delete.getType()));
		return inverse;
	}

	public Result execute(DeleteObject delete) {
		long indexTao = taoSelector.selectTao(delete.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		sparkService.delete(datasource, delete);
		return ResultBuilder.buildSuccess();
	}
	
}
