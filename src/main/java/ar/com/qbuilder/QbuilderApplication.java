package ar.com.qbuilder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.domain.DeleteAssociation;
import ar.com.qbuilder.domain.InsertAssociation;
import ar.com.qbuilder.domain.InsertObject;
import ar.com.qbuilder.domain.Selection;
import ar.com.qbuilder.service.TestService;
import ar.com.qbuilder.service.executor.Executor;

@EnableAutoConfiguration
@SpringBootApplication
public class QbuilderApplication {

	@Autowired
	TestService testService;
	
	@Autowired
	Executor executor;
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(QbuilderApplication.class, args);
		Config config = context.getBean(Config.class);
		TestService springBean = context.getBean(TestService.class);
				
		Executor executor = context.getBean(Executor.class);
		
		//InsertObject query = makeInsertObject();
		InsertAssociation query = makeInsertAssociation();
//		Selection query = new Selection("assocations");
//		DeleteAssociation query = makeDeleteAssociation();
		executor.execute(query); 
		
	}

	private static DeleteAssociation makeDeleteAssociation() {
		DeleteAssociation query = new DeleteAssociation();
		query.setLeftId(152L);
		query.setRightId(153L);
		query.setType(10);
		return query;
	}

	private static InsertAssociation makeInsertAssociation() {
		InsertAssociation association = new InsertAssociation();
		association.withLeftId(152L);
		association.setRightId(163L);
		association.setType(10);
		association.setInverseType(20);
		association.setTable("associations");
		return association;
	}

	private static InsertObject makeInsertObject() {
		InsertObject insertion = new InsertObject();
		insertion.setId(100203L);
		insertion.setTable("objects");
		insertion.setType(10);
		insertion.setObject("'{}'");
		return insertion;
	}

}
