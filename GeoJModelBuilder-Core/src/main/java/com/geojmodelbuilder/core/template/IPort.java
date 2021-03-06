/**
 * Copyright (C) 2013 - 2016 Wuhan University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.core.template;

import java.util.List;

import com.geojmodelbuilder.core.IExchange;
import com.geojmodelbuilder.core.data.IData;
import com.geojmodelbuilder.core.instance.IParameter;
import com.geojmodelbuilder.core.template.impl.SpatialMetadata;

/**
 * @author Mingda Zhang
 *
 */
public interface IPort extends IExchange{
	
	List<? extends IParameter> getInstances();
	
	/**
	 * The data candidates hold by this port.
	 */
	List<IData> getDataCandidates();
	
	/**
	 * The owner of the port.
	 * @return
	 */
	IProcessTemplate getOwner();
	
	/**
	 * spatial-temporal constraints of the port.
	 */
	SpatialMetadata getSptialDescription();
}
