package com.sapience.util;

import javax.persistence.Persistence;

public final class EntityManagerFactory {

	private static final javax.persistence.EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("sapience-service");

	private EntityManagerFactory() {
	}

	public static javax.persistence.EntityManagerFactory get() {
		return emfInstance;
	}

}