package ar.com.qbuilder.service.executor;

import java.util.List;

import javax.xml.crypto.Data;

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
		System.out.println("lalalalalla");
		long indexTao = taoSelector.selectTao(insertion.getId());
		List<String> queries = qbuilder.makeSentence(insertion);
		if(insertion.getObject() != null) {//InsertionType.Object
			Datasource datasource = taoSelector.getDatasource(indexTao);
			sparkService.run(queries.get(0), datasource, insertion.getTable());
		} else {//asociaciones
			
		}
	}

	
	
}
