/**
 *  Copyright 2012 Michael Vorburger (http://www.vorburger.ch)
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * PropertyChangeNotifier abstract base class.
 * 
 * Extend this, or copy/paste from it if you can't extend from it, to make a PropertyChangeNotifier.
 * 
 * Note that in the JavaBean specification, property values get updated before PropertyChangeListener gets called,
 * so you have to call super.firePropertyChange() AFTER you've changed the value of your property, e.g.
 * <code>firePropertyChange("name", this.name, this.name = name);</code> 
 * 
 * @see http://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeSupport.html
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("serial")
public abstract class AbstractPropertyChangeNotifier implements PropertyChangeNotifier {

	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
		changeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}
	
	// ---
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}
}
