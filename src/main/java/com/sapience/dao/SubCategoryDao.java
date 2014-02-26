package com.sapience.dao;

import javax.persistence.EntityManager;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.SubCategoryDaoImpl;
import com.sapience.model.SubCategory;

@ImplementedBy(SubCategoryDaoImpl.class)
public interface SubCategoryDao {

	SubCategory getSubCategoryBySubCategoryName(String subCategoryName);

	SubCategory saveSubCategory(EntityManager entityManager,
			String subCategoryName);

}