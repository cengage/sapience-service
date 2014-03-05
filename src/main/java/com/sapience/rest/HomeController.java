package com.sapience.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
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

		List<String> finalStatusList = new ArrayList<String>();

		finalStatusList.add("Jenkin Products Status");

		System.out.println("Jenkin Products Status");

		List<String> jenkinStatusList = app.fetchAndSaveJankinData();

		for (String jenkinStatus : jenkinStatusList) {
			finalStatusList.add(jenkinStatus);
		}

		finalStatusList.add("Jira Products Status");

		System.out.println("Jira Products Status");

		List<String> jiraStatusList = app.fetchAndSaveJiraData();

		for (String jiraStatus : jiraStatusList) {
			finalStatusList.add(jiraStatus);
		}

		Gson gson = new Gson();

		return gson.toJson(finalStatusList);

	}

}