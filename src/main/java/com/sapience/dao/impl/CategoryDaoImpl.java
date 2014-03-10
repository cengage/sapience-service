package com.sapience.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.sapience.dao.CategoryDao;
import com.sapience.model.Category;

public class CategoryDaoImpl extends GenericDAOImpl<AbstractEntity, Category>
		implements CategoryDao {

	@Override
	public Category getCategoryByCategoryName(String categoryName) {

		EntityManager localEntityManager = getEntityManagerFactory()
				.createEntityManager();
		CriteriaBuilder cb = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<Category> crit = cb.createQuery(Category.class);
		Root<Category> candidateRoot = crit.from(Category.class);

		Predicate categoryNameEquals = cb.equal(
				candidateRoot.get("categoryName"), categoryName);

		crit.where(categoryNameEquals);
		TypedQuery<Category> query = localEntityManager.createQuery(crit);
		List<Category> categoryList = query.getResultList();
		Category category = null;
		if (categoryList != null && categoryList.size() > 0) {
			category = categoryList.get(0);
		}
		localEntityManager.close();
		return category;
	}

	@Override
	public Category saveCategory(EntityManager entityManager,
			String categoryName) {

		if (entityManager == null) {
			EntityManager localEntityManager = getEntityManagerFactory()
					.createEntityManager();
			entityManager = localEntityManager;
		}

		Category category = new Category();
		category.setCategoryName(categoryName);
		category = entityManager.merge(category);
		return category;

	}

}