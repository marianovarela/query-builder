package ar.com.qbuilder.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import ar.com.qbuilder.domain.Deletion;
import ar.com.qbuilder.domain.Insertion;
import ar.com.qbuilder.domain.InsertionType;
import ar.com.qbuilder.domain.Query;
import ar.com.qbuilder.domain.Selection;
import ar.com.qbuilder.domain.Updation;
import ar.com.qbuilder.helper.TaoSelector;

@Component
public class QBuilder {
	
	private TaoSelector taoSelector;

	public List<String> makeSentence(Insertion insertion) {
		List<String> sentences = new LinkedList<String>();
		if(insertion.getInsertionType().equals(InsertionType.Object)) {
			String sentence = makeInsertObject(insertion);
			sentences.add(sentence);
		} else {//InsertionType.Object
			
		}
		
		return sentences;
	}

	private String makeInsertObject(Insertion insertion) { 
		String sentence = "INSERT INTO " + insertion.getTable() + " VALUES (id, type, object) (" + insertion.getId() + 
				", " + insertion.getType() + ", " + insertion.getObject() + ");";
		 
		return sentence;
	}
	
}
