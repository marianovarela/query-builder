package ar.com.qbuilder.service.executor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.aspect.InsertValidator;
import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Association;
import ar.com.qbuilder.domain.AssociationInsertion;
import ar.com.qbuilder.domain.Object;
import ar.com.qbuilder.domain.ObjectInsertion;
import ar.com.qbuilder.helper.TaoSelector;
import ar.com.qbuilder.service.QBuilder;
import ar.com.qbuilder.service.SparkService;

@Service
public class InsertionExecutor {
	
	@Autowired
	QBuilder qbuilder;
	
	@Autowired
	TaoSelector taoSelector;
	
	@Autowired
	SparkService sparkService;
	
	@InsertValidator()
	public void execute(ObjectInsertion insertion) {
		long indexTao = taoSelector.selectTao(insertion.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<ar.com.qbuilder.domain.Object> list = makeListToInsert(insertion);
		sparkService.writeObject(datasource, insertion.getTable(), list);
	}
	
	private List<ar.com.qbuilder.domain.Object> makeListToInsert(ObjectInsertion insertion) {
		List<ar.com.qbuilder.domain.Object> list = new ArrayList<>();
		ar.com.qbuilder.domain.Object obj = new ar.com.qbuilder.domain.Object();
		obj.setId(insertion.getId());
		obj.setData(insertion.getObject());
		obj.setType(insertion.getType());
		list.add(obj);
		
		return list;
	}

	public void execute(AssociationInsertion insertion) {
		long indexTao = taoSelector.selectTao(insertion.getLeftId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		List<ar.com.qbuilder.domain.Association> list = makeListToInsert(insertion);
		sparkService.writeAssociation(datasource, insertion.getTable(), list);
	}

	private List<Association> makeListToInsert(AssociationInsertion insertion) {
		List<ar.com.qbuilder.domain.Association> list = new ArrayList<>();
		ar.com.qbuilder.domain.Association association = new ar.com.qbuilder.domain.Association();
		association.setLeft_id(insertion.getLeftId());
		association.setRight_id(insertion.getRightId());
		association.setType(insertion.getType());
		list.add(association);
		
		return list;
	}
	
}
