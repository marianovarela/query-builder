package ar.com.qbuilder.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.ResultSet;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.helper.ResultBuilder;
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class SelectionService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;

	public ResultSet execute(SelectAssociation select) {
		long indexTao = taoSelector.selectTao(select.getLeftId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		Dataset<Row> result = sparkService.execute(datasource, select);
		return ResultBuilder.buildSuccess(result);
	}

	public ResultSet execute(SelectObject select) {
		long indexTao = taoSelector.selectTao(select.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		Dataset<Row> result = sparkService.execute(datasource, select);
		return ResultBuilder.buildSuccess(result);
	}

}
