/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.databinding.EMFDataBindingContext;

/**
 * DataBindingContext for Xtext DataBinding.
 * 
 * @see DataBindingContext
 * 
 * @author Michael Vorburger
 */
public class XtextDataBindingContext extends EMFDataBindingContext {

	public XtextDataBindingContext() {
		super();
	}

	public XtextDataBindingContext(Realm realm) {
		super(realm);
	}

}
