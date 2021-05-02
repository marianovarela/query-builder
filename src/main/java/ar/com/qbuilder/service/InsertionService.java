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
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class InsertionService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;

	@InsertValidator()
	public void execute(InsertObject insertion) {
		long indexTao = taoSelector.selectTao(insertion.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<ar.com.qbuilder.domain.Object> list = makeListToInsert(insertion);
		sparkService.writeObject(datasource, insertion.getTable(), list);
	}

	private List<ar.com.qbuilder.domain.Object> makeListToInsert(InsertObject insertion) {
		List<ar.com.qbuilder.domain.Object> list = new ArrayList<>();
		ar.com.qbuilder.domain.Object obj = new ar.com.qbuilder.domain.Object();
		obj.setId(insertion.getId());
		obj.setData(insertion.getObject());
		obj.setType(insertion.getType());
		list.add(obj);

		return list;
	}

	@InsertValidator()
	public void execute(InsertAssociation insertion) {
		List<ar.com.qbuilder.domain.Association> list = makeListToInsert(insertion);

		list.forEach((assoc) -> {
			long indexTao = taoSelector.selectTao(assoc.getLeft_id());
			Datasource datasource = taoSelector.getDatasource(indexTao);
			List<Association> assocList = new LinkedList<Association>();
			assocList.add(assoc);
			sparkService.writeAssociation(datasource, insertion.getTable(), assocList);
		});
	}

	private List<Association> makeListToInsert(InsertAssociation insertion) {
		List<ar.com.qbuilder.domain.Association> list = new ArrayList<>();
		ar.com.qbuilder.domain.Association association = new ar.com.qbuilder.domain.Association();
		association.setLeft_id(insertion.getLeftId());
		association.setRight_id(insertion.getRightId());
		association.setType(insertion.getType());
		list.add(association);

		ar.com.qbuilder.domain.Association associationInverse = new ar.com.qbuilder.domain.Association();
		associationInverse.setLeft_id(insertion.getRightId());
		associationInverse.setRight_id(insertion.getLeftId());
		associationInverse.setType(insertion.getInverseType());
		list.add(associationInverse);

		return list;
	}

}
