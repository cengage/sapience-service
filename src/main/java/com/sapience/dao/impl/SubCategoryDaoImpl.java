package com.sapience.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.sapience.dao.SubCategoryDao;
import com.sapience.model.SubCategory;

public class SubCategoryDaoImpl extends
		GenericDAOImpl<AbstractEntity, SubCategory> implements SubCategoryDao {

	@Override
	public SubCategory getSubCategoryBySubCategoryName(String subCategoryName) {

		EntityManager localEntityManager = getEntityManagerFactory()
				.createEntityManager();
		CriteriaBuilder cb = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery<SubCategory> crit = cb.createQuery(SubCategory.class);
		Root<SubCategory> candidateRoot = crit.from(SubCategory.class);

		Predicate subCategoryNameEquals = cb.equal(
				candidateRoot.get("subCategoryName"), subCategoryName);

		crit.where(subCategoryNameEquals);
		TypedQuery<SubCategory> query = localEntityManager.createQuery(crit);
		List<SubCategory> subCategoryList = query.getResultList();
		SubCategory subCategory = null;
		if (subCategoryList != null && subCategoryList.size() > 0) {
			subCategory = subCategoryList.get(0);
		}
		localEntityManager.close();
		return subCategory;

	}

	@Override
	public SubCategory saveSubCategory(EntityManager entityManager,
			String subCategoryName) {

		if (entityManager == null) {
			EntityManager localEntityManager = getEntityManagerFactory()
					.createEntityManager();
			entityManager = localEntityManager;
		}

		SubCategory subCategory = new SubCategory();
		subCategory.setSubCategoryName(subCategoryName);
		subCategory = entityManager.merge(subCategory);
		return subCategory;

	}

}