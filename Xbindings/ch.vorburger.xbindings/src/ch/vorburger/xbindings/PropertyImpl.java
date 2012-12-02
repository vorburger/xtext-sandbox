/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Property Implementation.
 * 
 * @author Michael Vorburger
 */
public class PropertyImpl<T> implements Property<T> {
	// TODO if in the end no other implementations are made, merge Impl & interface?
	
	private T value;
	private List<PropertyChangeListener> propertyChangeListeners;
	
	public PropertyImpl() {
		super();
	}
	
	public PropertyImpl(T value) {
		super();
		this.value = value;
	}

	@Override public void set(T newValue) { 
		value = newValue; 
		if (propertyChangeListeners != null) {
			for (PropertyChangeListener propertyChangeListener : propertyChangeListeners) {
				propertyChangeListener.changed(); 
			}
		}
	}
	
	@Override public T get() { 
		PropertyAccessTrackerUtil.INSTANCE.accessed(this); 
		return value; 
	}
	
	@Override public void addChangeListener(PropertyChangeListener cl) {
		if (cl == null)
			return;
		if (this.propertyChangeListeners == null)
			this.propertyChangeListeners = new ArrayList<>(1);
		if (!this.propertyChangeListeners.contains(propertyChangeListeners))
			this.propertyChangeListeners.add(cl);
	}
}