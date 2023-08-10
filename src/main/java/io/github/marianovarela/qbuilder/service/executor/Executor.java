package io.github.marianovarela.qbuilder.service.executor;

import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.marianovarela.qbuilder.domain.DeleteAssociation;
import io.github.marianovarela.qbuilder.domain.DeleteObject;
import io.github.marianovarela.qbuilder.domain.InsertAssociation;
import io.github.marianovarela.qbuilder.domain.InsertObject;
import io.github.marianovarela.qbuilder.domain.Join;
import io.github.marianovarela.qbuilder.domain.ResultSet;
import io.github.marianovarela.qbuilder.domain.SelectAssociation;
import io.github.marianovarela.qbuilder.domain.SelectCustom;
import io.github.marianovarela.qbuilder.domain.SelectObject;
import io.github.marianovarela.qbuilder.domain.Union;
import io.github.marianovarela.qbuilder.domain.UpdateAssociation;
import io.github.marianovarela.qbuilder.domain.UpdateObject;
import io.github.marianovarela.qbuilder.helper.AssociationKeys;
import io.github.marianovarela.qbuilder.helper.ResultBuilder;
import io.github.marianovarela.qbuilder.service.DeletionService;
import io.github.marianovarela.qbuilder.service.InsertionService;
import io.github.marianovarela.qbuilder.service.SelectionCustomService;
import io.github.marianovarela.qbuilder.service.SelectionService;
import io.github.marianovarela.qbuilder.service.UpdationService;

@Service
public class Executor {

	@Autowired 
	private InsertionService insertionService;
	
	@Autowired 
	private UpdationService updationService;
	
	@Autowired 
	private SelectionService selectionService;
	
	@Autowired 
	private DeletionService deletionService;
	
	@Autowired 
	private SelectionCustomService selectionCustomService;
	
	@Autowired
	private AssociationKeys associationKeys;
	
	public ResultSet execute(InsertObject insertion) {
		return insertionService.execute(insertion);
	}

	public ResultSet execute(InsertAssociation insertion) {
		return insertionService.execute(insertion);
	}

	public ResultSet execute(SelectAssociation query) {
		return selectionService.execute(query);
	}

	public ResultSet execute(DeleteAssociation query) {
		return deletionService.execute(query);
	}

	public ResultSet execute(DeleteObject query) {
		return deletionService.execute(query);
		
	}

	public ResultSet execute(SelectObject query) {
		return selectionService.execute(query);
	}

	public ResultSet execute(UpdateObject query) {
		return updationService.execute(query);
	}

	public ResultSet execute(UpdateAssociation query) {
		return updationService.execute(query);
	}

	public void setKeys(Map<Long, Long> keys) {
		associationKeys.keys = keys;
	}

	public ResultSet execute(SelectCustom query) {
		Dataset<Row> dataset = selectionCustomService.execute(query); 
		ResultSet result = ResultBuilder.buildSuccess(dataset);
		return result;
	}

	public ResultSet execute(Join join) {
		Dataset<Row> dataset = selectionCustomService.execute(join);
		ResultSet result = ResultBuilder.buildSuccess(dataset);
		return result;
	}

	public ResultSet execute(Union query) {
		Dataset<Row> dataset = selectionCustomService.execute(query);
		ResultSet result = ResultBuilder.buildSuccess(dataset);
		return result;
	}

}
