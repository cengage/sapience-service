package com.sapience.dao.impl;

import java.io.Serializable;

public abstract class AbstractEntity<P> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract P getId();
}