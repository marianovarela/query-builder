package ar.com.qbuilder.service;

import org.springframework.stereotype.Component;

import ar.com.qbuilder.domain.Deletion;
import ar.com.qbuilder.domain.Insertion;
import ar.com.qbuilder.domain.Query;
import ar.com.qbuilder.domain.Selection;
import ar.com.qbuilder.domain.Updation;

@Component
public class QBuilder {

	/**
	 * Selection from table specified in the @param table
	 * @param table
	 */
	public Selection select(String table) {
		Selection selection = new Selection(table);
		
		return selection;
	}
	
	public Insertion insert(String table) {
		//TODO
		return null;
	}
	
	public Updation update(String table) {
		//TODO
		return null;
	}
	
	public Deletion delete(String table) {
		//TODO
		return null;
	}
	
}
