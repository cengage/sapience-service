package com.sapience.dao.impl;

import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.sapience.dao.CategoryDao;
import com.sapience.dao.ConnectorDao;
import com.sapience.dao.ProductCategoryDao;
import com.sapience.dao.ProductDao;
import com.sapience.dao.SubCategoryDao;
import com.sapience.dao.SubCategoryDataDao;
import com.sapience.model.Category;
import com.sapience.model.Product;
import com.sapience.model.ProductCategory;
import com.sapience.model.SubCategory;
import com.sapience.model.SubCategoryData;

public class ConnectorDaoImpl extends GenericDAOImpl<AbstractEntity, Long>
		implements ConnectorDao {

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
		String status = null;

		try {

			EntityManager entityManager = getEntityManagerFactory()
					.createEntityManager();

			EntityTransaction entityTransaction = entityManager
					.getTransaction();
			entityTransaction.begin();

			// ----- save product ---------

			String productCode = fetchedData
					.get(fetchedData.keySet().toArray()[0]);

			Product product = productDao.getProductByProductCode(productCode);

			if (product == null) {
				product = productDao.saveProduct(entityManager, productCode);
			}

			// ---- save category -------

			String categoryName = fetchedData.get(fetchedData.keySet()
					.toArray()[1]);

			Category category = categoryDao
					.getCategoryByCategoryName(categoryName);

			if (category == null) {
				category = categoryDao
						.saveCategory(entityManager, categoryName);
			}

			// ---- for product_category -----

			ProductCategory productCategory = productCategoryDao
					.getProductCategoryByProductIdAndCategoryId(category,
							product);

			if (productCategory == null) {

				productCategory = productCategoryDao.saveProductCategory(
						entityManager, product, category);

			}

			// ----- for Sub_Category-----

			for (int i = 2; i < fetchedData.size(); i++) {

				String subCategoryName = (String) fetchedData.keySet()
						.toArray()[i];

				SubCategory subCategory = subCategoryDao
						.getSubCategoryBySubCategoryName(subCategoryName);

				if (subCategory == null) {
					subCategory = subCategoryDao.saveSubCategory(entityManager,
							subCategoryName);
				}

				// ----- Sub_Category_Data -----
				String subCategoryRawData = fetchedData.get(fetchedData
						.keySet().toArray()[i]);

				SubCategoryData subCategoryData = subCategoryDataDao
						.getSubCategoryDataByProductCategoryAndSubCategory(
								productCategory, subCategory);

				if (subCategoryData == null) {
					subCategoryData = subCategoryDataDao.saveSubCategoryData(
							entityManager, productCategory, subCategoryRawData,
							subCategory);
				} else {
					subCategoryData = subCategoryDataDao.updateSubCategoryData(
							subCategoryData, entityManager, productCategory,
							subCategoryRawData, subCategory,
							subCategoryData.getCreatedDate());
				}
			}

			entityTransaction.commit();
			status = "Data Fetched and saved successfully";
		} catch (Exception e) {
			e.printStackTrace();
			status = "Data Fetched but not saved Successfully";
		}
		return status;

	}

}