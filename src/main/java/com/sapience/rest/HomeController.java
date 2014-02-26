package com.sapience.rest;

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

		System.out.println("initial call");

	    String jenkinStatus = app.fetchAndSaveJankinData();

		System.out.println(jenkinStatus);

		String jiraStatus = app.fetchAndSaveJiraData();

		System.out.println(jiraStatus);

		return "home";
	}

}