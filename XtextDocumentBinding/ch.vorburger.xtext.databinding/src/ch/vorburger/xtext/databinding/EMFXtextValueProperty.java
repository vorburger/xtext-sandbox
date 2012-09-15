/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IReadAccess;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

import ch.vorburger.xtext.databinding.internal.XtextPropertyListener;


/**
 * Like EMFEditValueProperty, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
@SuppressWarnings({ "unchecked", "restriction" })
public class EMFXtextValueProperty extends EMFValuePropertyWithErrorLogging {

	public EMFXtextValueProperty(EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
	}

	@Override
	protected void doSetValue(final Object source, final Object value) {
		IWriteAccess<XtextResource> access = (IWriteAccess<XtextResource>) source;
	    access.modify(new IUnitOfWork.Void<XtextResource>() {
	    	@Override public void process(XtextResource state) throws Exception {
	    		// TODO Handling (via TDD) if it doesn't exist yet! Ideally, don't throw an error, but create it on-the-fly...
	    		EObject eObject = state.getContents().get(0);
	    		EMFXtextValueProperty.super.doSetValue(eObject, value);
	    	};
		});
	}

	@Override
	protected Object doGetValue(final Object source) {
		IReadAccess<XtextResource> access = (IReadAccess<XtextResource>) source;
		return access.readOnly(new IUnitOfWork<Object, XtextResource>() {
			@Override public Object exec(XtextResource state) throws Exception {
	    		// TODO Handling (via TDD) if it doesn't exist yet! Should probably return null and NOT create it on-the-fly?
				EObject eObject = state.getContents().get(0);
				return EMFXtextValueProperty.super.doGetValue(eObject);
				
			}
		});
	}
	
	// TODO Carefully test if this pattern above/below will really work for FeaturePath as well, not just for one root EStructuralFeature...
	
	@Override
	public INativePropertyListener adaptListener(final ISimplePropertyListener listener)  {
		return new XtextPropertyListener.XtextValuePropertyListener() {
	    	@Override
	        protected IProperty getOwner() {
	          return EMFXtextValueProperty.this;
	        }

	        @Override
	        protected ISimplePropertyListener getListener() {
	          return listener;
	        }

			@Override
	        protected EStructuralFeature getFeature() {
	          return EMFXtextValueProperty.this.getFeature();
	        }
	      };
	  }
}
