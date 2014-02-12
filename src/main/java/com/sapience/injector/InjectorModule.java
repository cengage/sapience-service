package com.sapience.injector;

import com.google.inject.AbstractModule;
import com.sapience.service.ConnectorService;
import com.sapience.service.GenericService;
import com.sapience.service.impl.ConnectorServiceImpl;
import com.sapience.service.impl.GenericServiceImpl;

public class InjectorModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(ConnectorService.class).to(ConnectorServiceImpl.class);
		bind(GenericService.class).to(GenericServiceImpl.class);

	}

}