package ar.com.qbuilder.service.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.Insertion;

@Service
public class Executor {

	@Autowired 
	private InsertionExecutor insertionExecutor;
	
	public void execute(Insertion insertion) {
		insertionExecutor.execute(insertion);
	}
	
}
