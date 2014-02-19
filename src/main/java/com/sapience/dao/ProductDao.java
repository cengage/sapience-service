package com.sapience.dao;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.ProductDaoImpl;
import com.sapience.model.Product;

@ImplementedBy(ProductDaoImpl.class)
public interface ProductDao {

	Product getProductByProductCode(String productCode);

	Product saveProduct(String productCode);

}