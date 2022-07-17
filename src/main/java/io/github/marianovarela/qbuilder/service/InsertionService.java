package io.github.marianovarela.qbuilder.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.aspect.InsertValidator;
import io.github.marianovarela.qbuilder.config.domain.Datasource;
import io.github.marianovarela.qbuilder.domain.Association;
import io.github.marianovarela.qbuilder.domain.InsertAssociation;
import io.github.marianovarela.qbuilder.domain.InsertObject;
import io.github.marianovarela.qbuilder.domain.ResultSet;
import io.github.marianovarela.qbuilder.helper.AssociationKeys;
import io.github.marianovarela.qbuilder.helper.ResultBuilder;
import io.github.marianovarela.qbuilder.helper.TaoSelector;

@Service
public class InsertionService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;
	
	@Autowired
	AssociationKeys associationKeys;

	@InsertValidator()
	public ResultSet execute(InsertObject insertion) {
		long indexTao = taoSelector.selectTao(insertion.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<io.github.marianovarela.qbuilder.domain.Object> list = makeListToInsert(insertion);
		sparkService.writeObject(datasource, insertion.getTable(), list);
		return ResultBuilder.buildSuccess();
	}

	private List<io.github.marianovarela.qbuilder.domain.Object> makeListToInsert(InsertObject insertion) {
		List<io.github.marianovarela.qbuilder.domain.Object> list = new ArrayList<>();
		io.github.marianovarela.qbuilder.domain.Object obj = new io.github.marianovarela.qbuilder.domain.Object();
		obj.setId(insertion.getId());
		obj.setData(insertion.getData());
		obj.setType(insertion.getType());
		list.add(obj);

		return list;
	}

	@InsertValidator()
	public ResultSet execute(InsertAssociation insertion) {
		List<io.github.marianovarela.qbuilder.domain.Association> list = makeListToInsert(insertion);
		
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
		List<io.github.marianovarela.qbuilder.domain.Association> list = new ArrayList<>();
		
		//atype
		io.github.marianovarela.qbuilder.domain.Association association = new io.github.marianovarela.qbuilder.domain.Association();
		association.setLeft_id(insertion.getLeftId());
		association.setRight_id(insertion.getRightId());
		association.setType(insertion.getType());
		list.add(association);

		//inverse
		if(insertion.getInverseType() != null) {
			io.github.marianovarela.qbuilder.domain.Association associationInverse = new io.github.marianovarela.qbuilder.domain.Association();
			associationInverse.setLeft_id(insertion.getRightId());
			associationInverse.setRight_id(insertion.getLeftId());
			associationInverse.setType(insertion.getInverseType());
			list.add(associationInverse);
		}

		return list;
	}

}
