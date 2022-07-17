package io.github.marianovarela.qbuilder.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

import io.github.marianovarela.qbuilder.config.domain.Datasource;

@Component
public class HibernateUtil {

    public SessionFactory getSessionFactory(Datasource datasource) {
	    Configuration cfg = new Configuration();
	    cfg.getProperties().setProperty("hibernate.connection.username", datasource.getUser());
	    cfg.getProperties().setProperty("hibernate.connection.password", datasource.getPassword());
	    cfg.getProperties().setProperty("hibernate.connection.url", datasource.getUrl() + "/" + datasource.getSchema());
	    cfg.getProperties().setProperty("hibernate.connection.driver_class", datasource.getDriver());
	    return cfg.buildSessionFactory();
    }

}
