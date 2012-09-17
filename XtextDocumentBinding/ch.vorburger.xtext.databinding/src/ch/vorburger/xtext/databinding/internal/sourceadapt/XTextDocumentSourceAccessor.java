/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal.sourceadapt;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;


/**
 * SourceAccessor based on IXtextResourceReadWriteAccess
 * (IReadAccess<XtextResource> & IWriteAccess<XtextResource>),
 * normally the XtextDocument.
 * 
 * <p>This can only access Features in the first EObject of
 * the Resource contents, and nothing "nested deeper inside".
 * For the purposed of getting a starting point for Data Binding,
 * that's good enough.  Nesting works through the 
 * </p>
 * 
 * <p>Notification Adapters are registered on the Resource.</p>
 * 
 * @author Michael Vorburger
 */
public class XTextDocumentSourceAccessor implements SourceAccessor {

	private final IXtextResourceReadWriteAccess access;

	public XTextDocumentSourceAccessor(IXtextResourceReadWriteAccess access) {
		this.access = access;
	}
	
	@Override
	public void eSet(final EStructuralFeature feature, final Object value) {
	    access.modify(new IUnitOfWork.Void<XtextResource>() {
	    	@Override public void process(XtextResource state) throws Exception {
	    		// TODO Handling (via TDD) if it doesn't exist yet! Ideally, don't throw an error, but create it on-the-fly...
	    		EObject rootEObject = rootEObject(state);
				if (rootEObject != null)
					rootEObject.eSet(feature, value);
				else
		    		throw new IllegalStateException("Uh uh, no content in Resource - why you asking to set it, calling code? Feature: " + feature +  ", Resource: " + state);
	    	};
		});
	}

	@Override
	public Object eGet(final EStructuralFeature feature) {
		return access.readOnly(new IUnitOfWork<Object, XtextResource>() {
			@Override public Object exec(XtextResource resource) throws Exception {
	    		// TODO Handling (via TDD) if it doesn't exist yet! Should probably return null and NOT create it on-the-fly?
				EObject rootEObject = rootEObject(resource);
				if (rootEObject != null)
					return rootEObject.eGet(feature);
				else
					return null;
			}
		});
	}

	protected EObject rootEObject(XtextResource resource) {
		final EList<EObject> contents = resource.getContents();
		if (!contents.isEmpty())
			return contents.get(0);
		else
			return null;
	}

	@Override
	public void addAdapter(Adapter adapter) {
		getResource().eAdapters().add(adapter);
	}

	@Override
	public void removeAdapter(Adapter adapter) {
		getResource().eAdapters().remove(adapter);
	}
	
	protected Resource getResource() {
		return access.readOnly(new IUnitOfWork<Resource, XtextResource>() {
			@Override public Resource exec(XtextResource state) throws Exception {
	    		return state;
			}
		});
	}

}
