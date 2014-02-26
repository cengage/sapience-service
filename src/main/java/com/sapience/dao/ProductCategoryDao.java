package com.sapience.dao;

import javax.persistence.EntityManager;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.ProductCategoryDaoImpl;
import com.sapience.model.Category;
import com.sapience.model.Product;
import com.sapience.model.ProductCategory;

@ImplementedBy(ProductCategoryDaoImpl.class)
public interface ProductCategoryDao {

	ProductCategory getProductCategoryByProductIdAndCategoryId(
			Category category, Product product);

	ProductCategory saveProductCategory(EntityManager entityManager,
			Product product, Category category);

}