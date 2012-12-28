/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings;

/**
 * Something which tells something else that a Property was (actually is
 * about to be) read once.
 * 
 * Implementations will most probably register a ChangeListener on the passed
 * ChangeNotifier to be made aware of future changes to the Property.
 * 
 * @author Michael Vorburger
 */
// intentionally just package local instead of public for now.. if this isn't needed by anything else, then may be remove it and keep only simple static method in XBinding
interface PropertyAccessListener {
	void accessed(PropertyChangeNotifier cn);
}