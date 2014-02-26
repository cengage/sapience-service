package com.sapience.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUB_CATEGORY_DATA")
public class SubCategoryData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "SUB_CATEGORY_RAW_DATA")
	private String subCategoryRawData;

	@Column(name = "SUB_CATEGORY_TARGET_DATA")
	private String subCategoryTargetData;

	@Column(name = "SUB_CATEGORY_CALCULATED_DATA")
	private String subCategoryCalculatedData;

	@Column(name = "CREATED_DATE")
	private Long createdDate;

	@Column(name = "LAST_MODIFIED_DATE")
	private Long lastModifiedDate;

	@ManyToOne
	@Column(name = "PRODUCT_CATEGORY_ID")
	private ProductCategory productCategory;

	@ManyToOne
	@Column(name = "SUB_CATEGORY_ID")
	private SubCategory subCategory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubCategoryRawData() {
		return subCategoryRawData;
	}

	public void setSubCategoryRawData(String subCategoryRawData) {
		this.subCategoryRawData = subCategoryRawData;
	}

	public String getSubCategoryTargetData() {
		return subCategoryTargetData;
	}

	public void setSubCategoryTargetData(String subCategoryTargetData) {
		this.subCategoryTargetData = subCategoryTargetData;
	}

	public String getSubCategoryCalculatedData() {
		return subCategoryCalculatedData;
	}

	public void setSubCategoryCalculatedData(String subCategoryCalculatedData) {
		this.subCategoryCalculatedData = subCategoryCalculatedData;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

}