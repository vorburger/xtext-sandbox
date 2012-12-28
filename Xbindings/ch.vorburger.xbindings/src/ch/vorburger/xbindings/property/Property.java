/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings.property;

import ch.vorburger.xbindings.PropertyChangeNotifier;

/**
 * Property which can be data bound (with Change Notification support).
 * 
 * @author Michael Vorburger
 */
public interface Property<T> extends PropertyChangeNotifier {
	
	/**
	 * Getter which invokes a PropertyAccessTracker.
	 * @return current value
	 */
	T get();
	
	/**
	 * Setter which notifies ChangeListener.
	 * @param newValue the new Value
	 */
	void set(T newValue);
}