package io.github.marianovarela.qbuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.config.domain.Datasource;
import io.github.marianovarela.qbuilder.domain.DeleteAssociation;
import io.github.marianovarela.qbuilder.domain.DeleteObject;
import io.github.marianovarela.qbuilder.domain.ResultSet;
import io.github.marianovarela.qbuilder.helper.AssociationKeys;
import io.github.marianovarela.qbuilder.helper.ResultBuilder;
import io.github.marianovarela.qbuilder.helper.TaoSelector;

@Service
public class DeletionService {

	@Autowired
	TaoSelector taoSelector;
	
	@Autowired
	SparkService sparkService;
	
	@Autowired
	AssociationKeys associationKeys;

	public ResultSet execute(DeleteAssociation delete) {
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

	public ResultSet execute(DeleteObject delete) {
		long indexTao = taoSelector.selectTao(delete.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		sparkService.delete(datasource, delete);
		return ResultBuilder.buildSuccess();
	}
	
}
