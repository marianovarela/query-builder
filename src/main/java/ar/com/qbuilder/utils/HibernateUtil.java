package ar.com.qbuilder.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

import ar.com.qbuilder.config.domain.Datasource;

@Component
public class HibernateUtil {

    public SessionFactory getSessionFactory(Datasource datasource) {
	    Configuration cfg = new Configuration();
	    cfg.getProperties().setProperty("hibernate.connection.username", datasource.getUser());
	    cfg.getProperties().setProperty("hibernate.connection.password", datasource.getPassword());
	    cfg.getProperties().setProperty("hibernate.connection.url", datasource.getUrl());
	    cfg.getProperties().setProperty("hibernate.connection.driver_class", datasource.getDriver());
	    cfg.getProperties().setProperty("hibernate.connection.default_schema", datasource.getSchema());
	    return cfg.buildSessionFactory();
    }

}
