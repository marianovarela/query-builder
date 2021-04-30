package ar.com.qbuilder.service.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.AssociationInsertion;
import ar.com.qbuilder.domain.ObjectInsertion;

@Service
public class Executor {

	@Autowired 
	private InsertionExecutor insertionExecutor;
	
	public void execute(ObjectInsertion insertion) {
		insertionExecutor.execute(insertion);
	}

	public void execute(AssociationInsertion insertion) {
		insertionExecutor.execute(insertion);
		
	}
	
}
