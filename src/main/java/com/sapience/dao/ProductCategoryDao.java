package com.sapience.dao;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.ProductCategoryDaoImpl;

@ImplementedBy(ProductCategoryDaoImpl.class)
public interface ProductCategoryDao {

}