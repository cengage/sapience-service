package com.sapience.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {

	private static final EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("sapience-service");

	private EMF() {
	}

	public static EntityManagerFactory get() {
		return emfInstance;
	}

}