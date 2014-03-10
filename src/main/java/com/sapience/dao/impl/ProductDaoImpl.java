package com.sapience.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.sapience.dao.ProductDao;
import com.sapience.model.Product;

public class ProductDaoImpl extends GenericDAOImpl<AbstractEntity, Product>
		implements ProductDao {

	@Override
	public Product getProductByProductCode(String productCode) {
		EntityManager localEntityManager = getEntityManagerFactory()
				.createEntityManager();
		CriteriaBuilder cb = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<Product> crit = cb.createQuery(Product.class);
		Root<Product> candidateRoot = crit.from(Product.class);

		Predicate productCodeEquals = cb.equal(
				candidateRoot.get("productCode"), productCode);

		crit.where(productCodeEquals);
		TypedQuery<Product> query = localEntityManager.createQuery(crit);
		List<Product> productList = query.getResultList();
		Product product = null;
		if (productList != null && productList.size() > 0) {
			product = productList.get(0);
		}
		localEntityManager.close();
		return product;
	}

	@Override
	public Product saveProduct(EntityManager entityManager, String productCode) {

		if (entityManager == null) {
			EntityManager localEntityManager = getEntityManagerFactory()
					.createEntityManager();
			entityManager = localEntityManager;
		}

		Product product = new Product();
		product.setProductCode(productCode);
		product = entityManager.merge(product);
		return product;

	}

}