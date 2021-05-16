package ar.com.qbuilder.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.aspect.InsertValidator;
import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Association;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Result;
import ar.com.qbuilder.helper.AssociationKeys;
import ar.com.qbuilder.helper.ResultBuilder;
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class InsertionService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;
	
	@Autowired
	AssociationKeys associationKeys;

	@InsertValidator()
	public Result execute(InsertObject insertion) {
		long indexTao = taoSelector.selectTao(insertion.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<ar.com.qbuilder.domain.Object> list = makeListToInsert(insertion);
		sparkService.writeObject(datasource, insertion.getTable(), list);
		return ResultBuilder.buildSuccess();
	}

	private List<ar.com.qbuilder.domain.Object> makeListToInsert(InsertObject insertion) {
		List<ar.com.qbuilder.domain.Object> list = new ArrayList<>();
		ar.com.qbuilder.domain.Object obj = new ar.com.qbuilder.domain.Object();
		obj.setId(insertion.getId());
		obj.setData(insertion.getData());
		obj.setType(insertion.getType());
		list.add(obj);

		return list;
	}

	@InsertValidator()
	public Result execute(InsertAssociation insertion) {
		List<ar.com.qbuilder.domain.Association> list = makeListToInsert(insertion);
		
		this.updateAssociationKeys(insertion.getType(), insertion.getInverseType());

		list.forEach((assoc) -> {
			long indexTao = taoSelector.selectTao(assoc.getLeft_id());
			Datasource datasource = taoSelector.getDatasource(indexTao);
			List<Association> assocList = new LinkedList<Association>();
			assocList.add(assoc);
			sparkService.writeAssociation(datasource, insertion.getTable(), assocList);
		});
		return ResultBuilder.buildSuccess();
	}
	
	private void updateAssociationKeys(Integer type, Integer inverseType) {
		this.associationKeys.keys.put(type, inverseType);
		if(inverseType != null) {
			this.associationKeys.keys.put(inverseType, type);
		}
	}

	private List<Association> makeListToInsert(InsertAssociation insertion) {
		List<ar.com.qbuilder.domain.Association> list = new ArrayList<>();
		
		//atype
		ar.com.qbuilder.domain.Association association = new ar.com.qbuilder.domain.Association();
		association.setLeft_id(insertion.getLeftId());
		association.setRight_id(insertion.getRightId());
		association.setType(insertion.getType());
		list.add(association);

		//inverse
		if(insertion.getInverseType() != null) {
			ar.com.qbuilder.domain.Association associationInverse = new ar.com.qbuilder.domain.Association();
			associationInverse.setLeft_id(insertion.getRightId());
			associationInverse.setRight_id(insertion.getLeftId());
			associationInverse.setType(insertion.getInverseType());
			list.add(associationInverse);
		}

		return list;
	}

}
