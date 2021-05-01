package ar.com.qbuilder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.domain.AssociationInsertion;
import ar.com.qbuilder.domain.ObjectInsertion;
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
		
		//ObjectInsertion query = makeInsertObject();
		AssociationInsertion query = makeInsertAssociation();
		executor.execute(query); 
		
	}

	private static AssociationInsertion makeInsertAssociation() {
		AssociationInsertion association = new AssociationInsertion();
		association.withLeftId(152L);
		association.setRightId(153L);
		association.setType(10);
		association.setInverseType(20);
		association.setTable("associations");
		return association;
	}

	private static ObjectInsertion makeInsertObject() {
		ObjectInsertion insertion = new ObjectInsertion();
		insertion.setId(100203L);
		insertion.setTable("objects");
		insertion.setType(10);
		insertion.setObject("'{}'");
		return insertion;
	}

}
