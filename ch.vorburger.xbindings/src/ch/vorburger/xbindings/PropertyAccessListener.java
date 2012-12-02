/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings;

/**
 * Something which tells something else that a Property was read (actually is
 * about to be) once.
 * 
 * Implementations will most probably register a ChangeListener on the passed
 * ChangeNotifier to be made aware of future changes to the Property.
 * 
 * @author Michael Vorburger
 */
public interface PropertyAccessListener {
	void accessed(PropertyChangeNotifier cn);
}