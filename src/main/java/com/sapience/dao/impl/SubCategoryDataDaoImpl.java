package com.sapience.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.sapience.dao.SubCategoryDataDao;
import com.sapience.model.ProductCategory;
import com.sapience.model.SubCategory;
import com.sapience.model.SubCategoryData;

public class SubCategoryDataDaoImpl implements SubCategoryDataDao {

	EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("sapience-service");

	@Override
	public SubCategoryData getSubCategoryDataByProductCategoryAndSubCategory(
			ProductCategory productCategory, SubCategory subCategory) {

		EntityManager localEntityManager = emf.createEntityManager();
		CriteriaBuilder cb = emf.getCriteriaBuilder();
		CriteriaQuery<SubCategoryData> crit = cb
				.createQuery(SubCategoryData.class);
		Root<SubCategoryData> candidateRoot = crit.from(SubCategoryData.class);

		Predicate productCategoryEquals = cb.equal(
				candidateRoot.get("productCategory"), productCategory);

		Predicate subCategoryEquals = cb.equal(
				candidateRoot.get("subCategory"), subCategory);

		crit.where(productCategoryEquals, subCategoryEquals);

		TypedQuery<SubCategoryData> query = localEntityManager
				.createQuery(crit);
		List<SubCategoryData> subCategoryDataList = query.getResultList();
		SubCategoryData subCategoryData = null;
		if (subCategoryDataList != null && subCategoryDataList.size() > 0) {
			subCategoryData = subCategoryDataList.get(0);
		}
		localEntityManager.close();
		return subCategoryData;

	}

	@Override
	public SubCategoryData saveSubCategoryData(EntityManager entityManager,
			ProductCategory productCategory, String subCategoryRawData,
			SubCategory subCategory) {

		if (entityManager == null) {
			EntityManager localEntityManager = emf.createEntityManager();
			entityManager = localEntityManager;
		}

		Date createdDate = new Date();
		SubCategoryData subCategoryData = new SubCategoryData();
		subCategoryData.setProductCategory(productCategory);
		subCategoryData.setSubCategory(subCategory);
		subCategoryData.setSubCategoryRawData(subCategoryRawData);
		subCategoryData.setCreatedDate(createdDate.getTime());
		subCategoryData.setLastModifiedDate(createdDate.getTime());
		subCategoryData = entityManager.merge(subCategoryData);
		return subCategoryData;

	}

	@Override
	public SubCategoryData updateSubCategoryData(
			SubCategoryData subCategoryData, EntityManager entityManager,
			ProductCategory productCategory, String subCategoryRawData,
			SubCategory subCategory, Long createdDate) {

		if (entityManager == null) {
			EntityManager localEntityManager = emf.createEntityManager();
			entityManager = localEntityManager;
		}

		Date modifiedDate = new Date();
		subCategoryData.setProductCategory(productCategory);
		subCategoryData.setSubCategory(subCategory);
		subCategoryData.setSubCategoryRawData(subCategoryRawData);
		subCategoryData.setCreatedDate(createdDate);
		subCategoryData.setLastModifiedDate(modifiedDate.getTime());
		subCategoryData = entityManager.merge(subCategoryData);
		return subCategoryData;

	}

}