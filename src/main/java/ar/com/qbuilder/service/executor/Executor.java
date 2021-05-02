package ar.com.qbuilder.service.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Selection;
import ar.com.qbuilder.service.DeletionService;
import ar.com.qbuilder.service.InsertionService;
import ar.com.qbuilder.service.SelectionService;

@Service
public class Executor {

	@Autowired 
	private InsertionService insertionService;
	
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

	public Object execute(Selection query) {
		return selectionService.execute(query);
	}

	public void execute(DeleteAssociation query) {
		deletionService.execute(query);
	}
	
}
