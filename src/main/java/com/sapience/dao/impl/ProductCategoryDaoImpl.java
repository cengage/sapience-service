package com.sapience.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.sapience.dao.ProductCategoryDao;
import com.sapience.model.Category;
import com.sapience.model.Product;
import com.sapience.model.ProductCategory;

public class ProductCategoryDaoImpl implements ProductCategoryDao {

	EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("sapience-service");

	@Override
	public ProductCategory getProductCategoryByProductIdAndCategoryId(
			Category category, Product product) {

		EntityManager localEntityManager = emf.createEntityManager();
		CriteriaBuilder cb = emf.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> crit = cb
				.createQuery(ProductCategory.class);
		Root<ProductCategory> candidateRoot = crit.from(ProductCategory.class);

		Predicate productEquals = cb.equal(candidateRoot.get("product"),
				product);

		Predicate categoryEquals = cb.equal(candidateRoot.get("category"),
				category);

		crit.where(productEquals,categoryEquals);

		TypedQuery<ProductCategory> query = localEntityManager
				.createQuery(crit);
		List<ProductCategory> productCategoryList = query.getResultList();
		ProductCategory productCategory = null;
		if (productCategoryList != null && productCategoryList.size() > 0) {
			productCategory = productCategoryList.get(0);
		}
		localEntityManager.close();
		return productCategory;
	}

	@Override
	public ProductCategory saveProductCategory(EntityManager entityManager,
			Product product, Category category) {

		if (entityManager == null) {
			EntityManager localEntityManager = emf.createEntityManager();
			entityManager = localEntityManager;
		}

		Date createdDate = new Date();
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProduct(product);
		productCategory.setCategory(category);
		productCategory.setCreatedDate(createdDate.getTime());
		productCategory.setLastModifiedDate(createdDate.getTime());
		productCategory = entityManager.merge(productCategory);
		return productCategory;

	}

}