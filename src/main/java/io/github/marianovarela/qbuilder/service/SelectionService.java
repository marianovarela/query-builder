package io.github.marianovarela.qbuilder.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.config.domain.Datasource;
import io.github.marianovarela.qbuilder.domain.ResultSet;
import io.github.marianovarela.qbuilder.domain.SelectAssociation;
import io.github.marianovarela.qbuilder.domain.SelectObject;
import io.github.marianovarela.qbuilder.helper.ResultBuilder;
import io.github.marianovarela.qbuilder.helper.TaoSelector;

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
