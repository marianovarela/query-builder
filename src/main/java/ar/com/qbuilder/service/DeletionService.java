package ar.com.qbuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.helper.TaoSelector;

@Service
public class DeletionService {

	@Autowired
	TaoSelector taoSelector;

	@Autowired
	SparkService sparkService;

	public void execute(DeleteAssociation query) {
		sparkService.delete();
		
	}
	
}
