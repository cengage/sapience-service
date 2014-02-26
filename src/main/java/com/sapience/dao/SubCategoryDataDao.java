package com.sapience.dao;

import javax.persistence.EntityManager;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.SubCategoryDataDaoImpl;
import com.sapience.model.ProductCategory;
import com.sapience.model.SubCategory;
import com.sapience.model.SubCategoryData;

@ImplementedBy(SubCategoryDataDaoImpl.class)
public interface SubCategoryDataDao {

	SubCategoryData getSubCategoryDataByProductCategoryAndSubCategory(
			ProductCategory productCategory, SubCategory subCategory);

	SubCategoryData saveSubCategoryData(EntityManager entityManager,
			ProductCategory productCategory, String subCategoryRawData,
			SubCategory subCategory);

	SubCategoryData updateSubCategoryData(SubCategoryData subCategoryData,
			EntityManager entityManager, ProductCategory productCategory,
			String subCategoryRawData, SubCategory subCategory, Long createdDate);

}