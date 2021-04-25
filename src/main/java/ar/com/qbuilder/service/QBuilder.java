package ar.com.qbuilder.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;
import ar.com.qbuilder.domain.Deletion;
import ar.com.qbuilder.domain.Insertion;
import ar.com.qbuilder.domain.InsertionType;
import ar.com.qbuilder.domain.Query;
import ar.com.qbuilder.domain.Selection;
import ar.com.qbuilder.domain.Updation;
import ar.com.qbuilder.helper.TaoSelector;

@Component
public class QBuilder {
	
	@Autowired
	private TaoSelector taoSelector;

	public List<String> makeSentence(Insertion insertion) {
		List<String> sentences = new LinkedList<String>();
		if(insertion.getObject() != null) {
			String sentence = makeInsertObject(insertion);
			System.out.println(sentence);
			sentences.add(sentence);
		} else {//InsertionType.Object
			
		}
		
		return sentences;
	}

	private String makeInsertObject(Insertion insertion) { 
		long indexTao = taoSelector.selectTao(insertion.getId());
		Datasource datasource = taoSelector.getDatasource(indexTao);
		String sentence = "INSERT INTO " + datasource.getSchema() + "." + insertion.getTable() + " (id, type, object) VALUES (" + insertion.getId() + 
				", " + insertion.getType() + ", " + insertion.getObject() + ");";
		 
		return sentence;
	}
	
}
