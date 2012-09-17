/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal.sourceadapt;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Adapter to transform {@link Object}s used as Observed Property Sources in Data Binding.
 *   
 * @author Michael Vorburger
 */
public interface SourceAccessor {

	void eSet(EStructuralFeature feature, Object value);
	
	Object eGet(EStructuralFeature feature);
	
	/**
	 * Adapts the underlying Source to a List to which EMF Adapters can be added for Change Notification.  
	 */
	void addAdapter(Adapter adapter);
	
	void removeAdapter(Adapter adapter);
	
}
