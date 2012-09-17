/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal.sourceadapt;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * SourceAccessor based on direct EObject access.
 * 
 * This is NOT suitable for Xtext EMF Models, because inside the XtextDocument
 * EObject come and go (the tree is constantly updated and object deleted and
 * recreated), and cannot not be referenced outside (e.g. by a Binding).
 * 
 * @author Michael Vorburger
 */
// NOTE This class is actually not yet used/needed here - I wrote it in anticipation of one day making EMFValueProperty more pluggable (this would be the "as-is" implementation)
public class EObjectSourceAccessor implements SourceAccessor {
	
	private final EObject eObject;
	
	public EObjectSourceAccessor(EObject eObject) {
		super();
		this.eObject = eObject;
	}

	@Override
	public void eSet(EStructuralFeature feature, Object value) {
		eObject.eSet(feature, value);
	}

	@Override
	public Object eGet(EStructuralFeature feature) {
		return eObject.eGet(feature);
	}

	@Override
	public void addAdapter(Adapter adapter) {
		eObject.eAdapters().add(adapter);
	}

	@Override
	public void removeAdapter(Adapter adapter) {
		eObject.eAdapters().remove(adapter);
	}

}
