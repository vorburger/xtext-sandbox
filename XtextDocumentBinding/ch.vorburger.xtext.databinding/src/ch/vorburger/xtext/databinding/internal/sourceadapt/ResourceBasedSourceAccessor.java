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
import org.eclipse.emf.ecore.resource.Resource;

/**
 * SourceAccessor based on storing {@link Resource} + an EMF URI Fragment of an EObject.
 * 
 * @author Michael Vorburger
 */
public class ResourceBasedSourceAccessor implements SourceAccessor {

	private final Resource resource;
	private final String uriFragment;

	public ResourceBasedSourceAccessor(EObject eObject) {
		super();
		resource = eObject.eResource();
		if (resource != null) {
			uriFragment = resource.getURIFragment(eObject);
			System.out.println(eObject + " :::" + uriFragment);
		} else {
			throw new IllegalArgumentException("EObject has no eResource to observe: " + eObject.toString());
		}
	}
	
	@Override
	public void eSet(EStructuralFeature feature, Object value) {
		final EObject eObject = resource.getEObject(uriFragment);
		if (eObject == null)
			throw new IllegalStateException("Huh?! resource.getEObject(uriFragment) == null on Resource " + resource + ", for URI Fragement " + uriFragment);
		eObject.eSet(feature, value);
	}

	@Override
	public Object eGet(EStructuralFeature feature) {
		final EObject eObject = resource.getEObject(uriFragment);
		if (eObject == null)
			throw new IllegalStateException("Huh?! resource.getEObject(uriFragment) == null on Resource " + resource + ", for URI Fragement " + uriFragment);
		return eObject.eGet(feature);
	}

	@Override
	public void addAdapter(Adapter adapter) {
		resource.eAdapters().add(adapter);
	}

	@Override
	public void removeAdapter(Adapter adapter) {
		resource.eAdapters().remove(adapter);
	}

}
