package ar.com.qbuilder.service.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.domain.Insertion;
import ar.com.qbuilder.domain.Query;


public class Executor {

	@Autowired 
	private InsertionExecutor insertionExecutor;
	
	public void execute(Insertion insertion) {
		insertionExecutor.execute(insertion);
	}
	
}
