package com.sapience.dao;

import javax.persistence.EntityManager;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.ProductDaoImpl;
import com.sapience.model.Product;

@ImplementedBy(ProductDaoImpl.class)
public interface ProductDao {

	Product getProductByProductCode(String productCode);

	Product saveProduct(EntityManager entityManager, String productCode);

}