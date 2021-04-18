package ar.com.qbuilder.service.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.domain.Insertion;
import ar.com.qbuilder.helper.TaoSelector;
import ar.com.qbuilder.service.QBuilder;

//@Component
public class InsertionExecutor {
	
	@Autowired
	QBuilder qbuilder;
	
	@Autowired
	TaoSelector taoSelector;
	
	public void execute(Insertion insertion) {
		// crea el string
		List<String> query = qbuilder.makeSentence(insertion);
		long indexTao = taoSelector.selectTao(insertion.getId());
		//invoca a spark
		//TODO
	}

	
	
}
