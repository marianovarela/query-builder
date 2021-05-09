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

}
