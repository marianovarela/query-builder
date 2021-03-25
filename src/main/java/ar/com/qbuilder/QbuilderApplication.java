package ar.com.qbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ar.com.qbuilder.config.Config;
import ar.com.qbuilder.service.TestService;

@EnableAutoConfiguration
@SpringBootApplication
public class QbuilderApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(QbuilderApplication.class, args);
		Config config = context.getBean(Config.class);

		System.out.println(config.getArity());
		System.out.println((13 % 10));
		System.out.println((13 % config.getArity()));
		TestService service = new TestService();
		service.test();
	}

}
