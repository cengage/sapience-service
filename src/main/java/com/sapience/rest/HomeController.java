package com.sapience.rest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sapience.injector.InjectorModule;
import com.sapience.web.MyApplication;

@Path("/home")
public class HomeController {

	Injector injector = Guice.createInjector(new InjectorModule());

	MyApplication app = injector.getInstance(MyApplication.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String home() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sapience-service");
        EntityManager em = emf.createEntityManager();

        System.out.println("initial call");

		String jenkinStatus = app.fetchAndSaveJankinData();

		System.out.println(jenkinStatus);

		String jiraStatus = app.fetchAndSaveJiraData();

		System.out.println(jiraStatus);

		return "home";
	}

}