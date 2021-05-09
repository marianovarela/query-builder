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
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class UpdationService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;

	public Object execute(UpdateObject update) {
		// TODO Esto se repite en insertionservice
		long indexTao = taoSelector.selectTao(update.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<ar.com.qbuilder.domain.Object> list = makeListToInsert(update);
		Object result = sparkService.updateObject(datasource, update.getTable(), list);
		return result; 
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
		Association assoc = makeListToUpdate(updation);
		long indexTao = taoSelector.selectTao(assoc.getLeft_id());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<Association> assocList = new LinkedList<Association>();
		assocList.add(assoc);
		sparkService.updateAssociation(datasource, updation.getTable(), assocList);
		return null;
	}

	private Association makeListToUpdate(UpdateAssociation updation) {
		ar.com.qbuilder.domain.Association association = new ar.com.qbuilder.domain.Association();
		association.setLeft_id(updation.getLeftId());
		association.setRight_id(updation.getRightId());
		association.setType(updation.getNewType());
		return association;
	}

}
