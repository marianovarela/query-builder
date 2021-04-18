package ar.com.qbuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.service.TestService;

@EnableAutoConfiguration
@SpringBootApplication
public class QbuilderApplication {

	@Autowired
	TestService testService;
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(QbuilderApplication.class, args);
		Config config = context.getBean(Config.class);
//		TestService service = new TestService();
//		service.test();
		TestService springBean = context.getBean(TestService.class);
		springBean.test();
		
//		System.out.println(config.getURL(0));
//		Datasource ds = null;
//		try {
//			ds = config.getDatasource(0);
//			System.out.println(ds.getDriver());
//			int n = 9;
//			System.out.println(n);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(config.getArity());
//		System.out.println((13 % 10));
//		System.out.println((13 % config.getArity()));
		
		
		
//		QBuilder qbuilder = new QBuilder();
//		qbuilder.select("objects");
		
//		Executor executor = new Executor(); 
//		int id = 123;
//		Insertion insertion = new Insertion();
//		insertion.withObject("objects", id, 10, null);
//		executor.execute(insertion);
	}

}
