package com.sapience.dao.impl;

import java.io.Serializable;

public abstract class AbstractEntity<P> implements Serializable {
	public abstract P getId();
}