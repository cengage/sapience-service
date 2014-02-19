package com.sapience.dao.impl;

import java.util.Map;

import javax.inject.Inject;

import com.sapience.dao.CategoryDao;
import com.sapience.dao.ConnectorDao;
import com.sapience.dao.ProductCategoryDao;
import com.sapience.dao.ProductDao;
import com.sapience.dao.SubCategoryDao;
import com.sapience.dao.SubCategoryDataDao;
import com.sapience.model.Category;
import com.sapience.model.Product;

public class ConnectorDaoImpl implements ConnectorDao {

	private ProductDao productDao;

	private CategoryDao categoryDao;

	private ProductCategoryDao productCategoryDao;

	private SubCategoryDao subCategoryDao;

	private SubCategoryDataDao subCategoryDataDao;

	public ProductDao getProductDao() {
		return productDao;
	}

	@Inject
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	@Inject
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public ProductCategoryDao getProductCategoryDao() {
		return productCategoryDao;
	}

	@Inject
	public void setProductCategoryDao(ProductCategoryDao productCategoryDao) {
		this.productCategoryDao = productCategoryDao;
	}

	public SubCategoryDao getSubCategoryDao() {
		return subCategoryDao;
	}

	@Inject
	public void setSubCategoryDao(SubCategoryDao subCategoryDao) {
		this.subCategoryDao = subCategoryDao;
	}

	public SubCategoryDataDao getSubCategoryDataDao() {
		return subCategoryDataDao;
	}

	@Inject
	public void setSubCategoryDataDao(SubCategoryDataDao subCategoryDataDao) {
		this.subCategoryDataDao = subCategoryDataDao;
	}

	@Override
	public String saveAllFetchedData(Map<String, String> fetchedData) {
		String status = "Data Saved Successfully";

		// ----- save product ---------
		String productCode = fetchedData.get(fetchedData.keySet().toArray()[0]);

		Product product = productDao.getProductByProductCode(productCode);

		if (product == null) {
			product = productDao.saveProduct(productCode);
		}

		// ---- save category -------

		String categoryName = fetchedData
				.get(fetchedData.keySet().toArray()[1]);

		Category category = categoryDao.getCategoryByCategoryName(categoryName);

		if (category == null) {
			category = categoryDao.saveCategory(categoryName);
		}

		return status;
	}

}