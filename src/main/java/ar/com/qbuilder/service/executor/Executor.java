package ar.com.qbuilder.service.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.SelectAssociation;
import ar.com.qbuilder.domain.SelectObject;
import ar.com.qbuilder.domain.UpdateAssociation;
import ar.com.qbuilder.domain.UpdateObject;
import ar.com.qbuilder.service.DeletionService;
import ar.com.qbuilder.service.InsertionService;
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
	
	public void execute(InsertObject insertion) {
		insertionService.execute(insertion);
	}

	public void execute(InsertAssociation insertion) {
		insertionService.execute(insertion);
	}

	public Object execute(SelectAssociation query) {
		return selectionService.execute(query);
	}

	public void execute(DeleteAssociation query) {
		deletionService.execute(query);
	}

	public Object execute(SelectObject query) {
		return selectionService.execute(query);
	}

	public Object execute(UpdateObject query) {
		return updationService.execute(query);
	}

	public Object execute(UpdateAssociation query) {
		return updationService.execute(query);
	}
	
}
