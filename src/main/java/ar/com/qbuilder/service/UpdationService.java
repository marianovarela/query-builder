package ar.com.qbuilder.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.aspect.UpdateAssociationValidator;
import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Association;
import ar.com.qbuilder.domain.UpdateAssociation;
import ar.com.qbuilder.domain.UpdateObject;
import ar.com.qbuilder.helper.AssociationKeys;
import ar.com.qbuilder.helper.ResultBuilder;
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class UpdationService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;
	
	@Autowired
	AssociationKeys associationKeys;

	public Object execute(UpdateObject update) {
		// TODO Esto se repite en insertionservice
		long indexTao = taoSelector.selectTao(update.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<ar.com.qbuilder.domain.Object> list = makeListToInsert(update);
		sparkService.updateObject(datasource, update.getTable(), list);
		return ResultBuilder.buildSuccess(); 
	}

	private List<ar.com.qbuilder.domain.Object> makeListToInsert(UpdateObject updation) {
		// TODO Esto se repite en insertionservice
		List<ar.com.qbuilder.domain.Object> list = new ArrayList<>();
		ar.com.qbuilder.domain.Object obj = new ar.com.qbuilder.domain.Object();
		obj.setId(updation.getId());
		obj.setData(updation.getData());
		obj.setType(updation.getType());
		list.add(obj);

		return list;
	}

	@UpdateAssociationValidator
	public Object execute(UpdateAssociation updation) {
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
		ar.com.qbuilder.domain.Association association = new ar.com.qbuilder.domain.Association();
		association.setLeft_id(updation.getLeftId());
		association.setRight_id(updation.getRightId());
		association.setType(updation.getType());
		association.setTime(updation.getTime());
		association.setData(updation.getData());
		
		return association;
	}

}
