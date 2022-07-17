package io.github.marianovarela.qbuilder.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.aspect.UpdateAssociationValidator;
import io.github.marianovarela.qbuilder.config.domain.Datasource;
import io.github.marianovarela.qbuilder.domain.Association;
import io.github.marianovarela.qbuilder.domain.ResultSet;
import io.github.marianovarela.qbuilder.domain.UpdateAssociation;
import io.github.marianovarela.qbuilder.domain.UpdateObject;
import io.github.marianovarela.qbuilder.helper.AssociationKeys;
import io.github.marianovarela.qbuilder.helper.ResultBuilder;
import io.github.marianovarela.qbuilder.helper.TaoSelector;

@Service
public class UpdationService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;
	
	@Autowired
	AssociationKeys associationKeys;

	public ResultSet execute(UpdateObject update) {
		// TODO Esto se repite en insertionservice
		long indexTao = taoSelector.selectTao(update.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<io.github.marianovarela.qbuilder.domain.Object> list = makeListToInsert(update);
		sparkService.updateObject(datasource, update.getTable(), list);
		return ResultBuilder.buildSuccess(); 
	}

	private List<io.github.marianovarela.qbuilder.domain.Object> makeListToInsert(UpdateObject updation) {
		// TODO Esto se repite en insertionservice
		List<io.github.marianovarela.qbuilder.domain.Object> list = new ArrayList<>();
		io.github.marianovarela.qbuilder.domain.Object obj = new io.github.marianovarela.qbuilder.domain.Object();
		obj.setId(updation.getId());
		obj.setData(updation.getData());
		obj.setType(updation.getType());
		list.add(obj);

		return list;
	}

	@UpdateAssociationValidator
	public ResultSet execute(UpdateAssociation updation) {
		updateAssociation(updation);
		
		// if inverse type not is null then update inverse type
		if(associationKeys.getInverse(updation.getType()) != null) {
			UpdateAssociation inverse = getInverse(updation);
			updateAssociation(inverse);
		}
		
		return ResultBuilder.buildSuccess();
	}

	private UpdateAssociation getInverse(UpdateAssociation updation) {
		UpdateAssociation inverse = new UpdateAssociation();
		inverse.setData(updation.getData());
		inverse.setLeftId(updation.getRightId());
		inverse.setRightId(updation.getLeftId());
		inverse.setTable(updation.getTable());
		inverse.setTime(updation.getTime());
		inverse.setType(associationKeys.getInverse(updation.getType()));
		return inverse;
	}

	private void updateAssociation(UpdateAssociation updation) {
		Association assoc = makeListToUpdate(updation);
		long indexTao = taoSelector.selectTao(assoc.getLeft_id());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<Association> assocList = new LinkedList<Association>();
		assocList.add(assoc);
		sparkService.updateAssociation(datasource, updation.getTable(), assocList);
	}

	private Association makeListToUpdate(UpdateAssociation updation) {
		io.github.marianovarela.qbuilder.domain.Association association = new io.github.marianovarela.qbuilder.domain.Association();
		association.setLeft_id(updation.getLeftId());
		association.setRight_id(updation.getRightId());
		association.setType(updation.getType());
		association.setTime(updation.getTime());
		association.setData(updation.getData());
		
		return association;
	}

}
