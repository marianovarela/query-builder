package ar.com.qbuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.Selection;
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class SelectionService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;

	public Object execute(Selection select) {
//		long indexTao = taoSelector.selectTao(select.getId());
//		Datasource datasource = taoSelector.getDatasource(indexTao);
		return null;
	}

}
