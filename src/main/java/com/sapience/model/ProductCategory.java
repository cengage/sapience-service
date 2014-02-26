package com.sapience.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade=CascadeType.ALL)
	@Column(name="PRODUCT_ID")
	private Product product;

	@ManyToOne(cascade=CascadeType.ALL)
	@Column(name="CATEGORY_ID")
	private Category category;

	@Column(name = "CATEGORY_RAW_DATA")
	private String categoryRawData;

	@Column(name = "CATEGORY_TARGET_DATA")
	private String categoryTargetData;

	@Column(name = "CREATED_DATE")
	private Long createdDate;

	@Column(name = "LAST_MODIFIED_DATE")
	private Long lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getCategoryRawData() {
		return categoryRawData;
	}

	public void setCategoryRawData(String categoryRawData) {
		this.categoryRawData = categoryRawData;
	}

	public String getCategoryTargetData() {
		return categoryTargetData;
	}

	public void setCategoryTargetData(String categoryTargetData) {
		this.categoryTargetData = categoryTargetData;
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

}