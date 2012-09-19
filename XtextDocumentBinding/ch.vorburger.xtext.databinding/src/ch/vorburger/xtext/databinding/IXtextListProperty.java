/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;


/**
 * Like IEMFListProperty, but using an IXtextResourceReadWriteAccess instead of an direct EObject access (or an EditingDomain).
 *
 * @author Michael Vorburger
 */
public interface IXtextListProperty { // TODO HIGH extends IXtextProperty, IEMFListProperty
	// TODO HIGH Write (and test) an implementation of this!
	
	IXtextListProperty values(IXtextValueProperty property);

}
