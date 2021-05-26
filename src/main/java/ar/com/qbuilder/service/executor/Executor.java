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
import ar.com.qbuilder.domain.Result;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectCustom;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.domain.UpdateAssociation;
import ar.com.qbuilder.domain.UpdateObject;
import ar.com.qbuilder.helper.AssociationKeys;
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
	
	public Result execute(InsertObject insertion) {
		return insertionService.execute(insertion);
	}

	public Result execute(InsertAssociation insertion) {
		return insertionService.execute(insertion);
	}

	public Result execute(SelectAssociation query) {
		return selectionService.execute(query);
	}

	public Result execute(DeleteAssociation query) {
		return deletionService.execute(query);
	}

	public Result execute(DeleteObject query) {
		return deletionService.execute(query);
		
	}

	public Result execute(SelectObject query) {
		return selectionService.execute(query);
	}

	public Result execute(UpdateObject query) {
		return updationService.execute(query);
	}

	public Result execute(UpdateAssociation query) {
		return updationService.execute(query);
	}

	public void setKeys(Map<Integer, Integer> keys) {
		associationKeys.keys = keys;
	}

	public Row[] execute(SelectCustom query) {
		Row[] result = (Row[]) selectionCustomService.execute(query); 
		return result;
	}

}
