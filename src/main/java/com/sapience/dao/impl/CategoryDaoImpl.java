package com.sapience.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

import com.sapience.dao.CategoryDao;
import com.sapience.model.Category;

public class CategoryDaoImpl implements CategoryDao {

	@PersistenceUnit(unitName = "sapience-service")
	private EntityManager entityManager;

	@Override
	public Category getCategoryByCategoryName(String categoryName) {
		Category category = entityManager.find(Category.class, categoryName);
		return category;
	}

	@Override
	public Category saveCategory(String categoryName) {
		Category category = new Category();
		category.setCategoryName(categoryName);
		category = entityManager.merge(category);
		return category;
	}

}