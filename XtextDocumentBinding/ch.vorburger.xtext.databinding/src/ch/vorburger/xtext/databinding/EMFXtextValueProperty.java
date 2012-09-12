/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;


/**
 * Like EMFEditValueProperty, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
public class EMFXtextValueProperty extends EMFValuePropertyWithErrorLogging {

	private final IXtextResourceReadWriteAccess access;

	public EMFXtextValueProperty(IXtextResourceReadWriteAccess xTextAccess, EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
		this.access = xTextAccess;
	}

	@Override
	protected void doSetValue(final Object source, final Object value) {
	    access.modify(new IUnitOfWork.Void<XtextResource>() {
	    	@Override public void process(XtextResource state) throws Exception {
	    		assertSameResource(source, state);
	    		EMFXtextValueProperty.super.doSetValue(source, value);
	    	};
		});
	}

	@Override
	protected Object doGetValue(final Object source) {
		return access.readOnly(new IUnitOfWork<Object, XtextResource>() {
			@Override public Object exec(XtextResource state) throws Exception {
				assertSameResource(source, state);
				return EMFXtextValueProperty.super.doGetValue(source);
			}
		});
	}

	/**
	 * Helper to just make sure we're on the same page;
	 * is the XtextResource passed to the IUnitOfWork really the resource of our EObject?
	 */
	protected void assertSameResource(Object source, XtextResource state) {
		// TODO later do this potentially somewhat costly type casting only if org.eclipse.core.databinding.util.Policy#DEFAULT ?
		EObject eObj = (EObject)source;
		Resource resource = eObj.eResource();
		XtextResource xtextResource = (XtextResource) resource;
		// TODO This null check shouldn't be needed ?!? Better throw exception (and log, see Bugzilla issue), instead just ignoring (because the DefaultTextEditComposer won't work!)
		if (xtextResource != null && !xtextResource.equals(state)) {
			// TODO IStatus status;
			// Policy.getLog().log(status);
			throw new RuntimeException("Whoa, we're not on the same page - the eResource of my object and the XtextResource passed to the IUnitOfWork are not the same - how come?!");
		}
	}

}
