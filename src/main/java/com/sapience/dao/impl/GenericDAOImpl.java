package com.sapience.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.sapience.dao.GenericDAO;

public abstract class GenericDAOImpl<E extends AbstractEntity, P> implements
		GenericDAO<E, P> {

	EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory("sapience-service");

	private Class<E> entityClass;

	private EntityManager entityManager = entityManagerFactory
			.createEntityManager();

	public EntityManagerFactory getEntityManagerFactory() {
		return this.entityManagerFactory;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public GenericDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	public GenericDAOImpl(final Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public GenericDAOImpl(final Class<E> entityClass,
			EntityManager entityManager) {
		this.entityClass = entityClass;
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public P insert(E entity) {
		entityManager.persist(entity);
		return (P) entity.getId();
	}

	public E find(P id) {
		return entityManager.find(getEntityClass(), id);
	}

	public void update(E entity) {
		entityManager.merge(entity);
	}

	public void delete(E entity) {
		entityManager.remove(entity);
	}

	@SuppressWarnings("unchecked")
	public Class<E> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;

				entityClass = (Class<E>) paramType.getActualTypeArguments()[0];

			} else {
				throw new IllegalArgumentException(
						"Could not guess entity class by reflection");
			}
		}
		return entityClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		return this.entityManager.createNamedQuery(
				getEntityClass().getSimpleName() + ".GetAll").getResultList();
	}

}