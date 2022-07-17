package ar.com.qbuilder.service.executor;

import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.DeleteObject;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Join;
import ar.com.qbuilder.domain.ResultSet;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.domain.Union;
import ar.com.qbuilder.domain.UpdateAssociation;
import ar.com.qbuilder.domain.UpdateObject;
import ar.com.qbuilder.helper.AssociationKeys;
import ar.com.qbuilder.helper.ResultBuilder;
import ar.com.qbuilder.service.DeletionService;
import ar.com.qbuilder.service.InsertionService;
import ar.com.qbuilder.service.SelectionCustomService;
import ar.com.qbuilder.service.SelectionService;
import ar.com.qbuilder.service.UpdationService;

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

	public void setKeys(Map<Integer, Integer> keys) {
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
