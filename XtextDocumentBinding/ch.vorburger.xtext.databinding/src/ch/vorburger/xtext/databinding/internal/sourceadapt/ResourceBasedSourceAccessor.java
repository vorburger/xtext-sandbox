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
	// TODO This class is not actually used here, anymore - probably delete it later, as it makes no sense/has no use anymore. If for any reason I end up keeping it, should refactor to share code with XTextDocumentSourceAccessor instead of having copy/pasted it in there

	private final Resource resource;
	private final String uriFragment;

	/**
	 * Constructor.
	 * 
	 * @param eObject to access later. Implementation keeps URI Fragment of this
	 *            EOBject in Resource - but shall never "hang on" to (keep a reference)
	 *            to this object! 
	 */
	public ResourceBasedSourceAccessor(EObject eObject) {
		super();
		resource = eObject.eResource();
		if (resource != null) {
			uriFragment = resource.getURIFragment(eObject);
			if (uriFragment == null)
				throw new IllegalArgumentException("Resource returned null for URI Fragment for EObject: " + eObject.toString());
		} else {
			throw new IllegalArgumentException("EObject has no eResource to observe: " + eObject.toString());
		}
	}
	
	@Override
	public void eSet(EStructuralFeature feature, Object value) {
		final EObject eObject = resource.getEObject(uriFragment);
		if (eObject == null)
			throw new IllegalStateException("Huh?! resource.getEObject(uriFragment) == null on Resource " + resource + ", for URI Fragment " + uriFragment);
		eObject.eSet(feature, value);
	}

	@Override
	public Object eGet(EStructuralFeature feature) {
		final EObject eObject = resource.getEObject(uriFragment);
		if (eObject == null)
			return null;
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
