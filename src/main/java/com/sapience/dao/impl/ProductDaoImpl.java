package com.sapience.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

import com.sapience.dao.ProductDao;
import com.sapience.model.Product;

public class ProductDaoImpl implements ProductDao {

	@PersistenceUnit(unitName = "sapience-service")
	private EntityManager entityManager;

	@Override
	public Product getProductByProductCode(String productCode) {

		Product product = entityManager.find(Product.class, productCode);
		return product;
	}

	@Override
	public Product saveProduct(String productCode) {

		Product product = new Product();
		product.setProductCode(productCode);
		product = entityManager.merge(product);
		return product;

	}

}