package ar.com.qbuilder.service.executor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Insertion;
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
	
	public void execute(Insertion insertion) {
		// crea el string
		long indexTao = taoSelector.selectTao(insertion.getId());
		if(insertion.getObject() != null) {//InsertionType.Object
			Datasource datasource = taoSelector.getDatasource(indexTao);
			List<ar.com.qbuilder.domain.Object> list = makeListToInsert(insertion);
			sparkService.write(datasource, insertion.getTable(), list);
		} else {//asociaciones
			
		}
	}

	private List<ar.com.qbuilder.domain.Object> makeListToInsert(Insertion insertion) {
		List<ar.com.qbuilder.domain.Object> list = new ArrayList<>();
		ar.com.qbuilder.domain.Object obj = new ar.com.qbuilder.domain.Object();
		obj.setId(insertion.getId());
		obj.setData(insertion.getObject());
		obj.setType(insertion.getType());
		list.add(obj);
		return list;
	}
	
}
