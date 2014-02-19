package com.sapience.dao;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.CategoryDaoImpl;
import com.sapience.model.Category;

@ImplementedBy(CategoryDaoImpl.class)
public interface CategoryDao {

	Category getCategoryByCategoryName(String categoryName);

	Category saveCategory(String categoryName);

}