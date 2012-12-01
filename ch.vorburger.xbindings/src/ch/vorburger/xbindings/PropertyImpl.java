/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings;

/**
 * Simple Property Implementation.
 * 
 * @author Michael Vorburger
 */
public class PropertyImpl<T> implements Property<T> {
	T value;
	ChangeListener cl;
	@Override public void set(T newValue) { value = newValue; if (cl != null) cl.changed(); }
	@Override public T get() { PropertyAccessTrackerUtil.INSTANCE.accessed(this); return value; }
	@Override public void setChangeListener(ChangeListener cl) {
		this.cl = cl;
	}
}