/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IReadAccess;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

import ch.vorburger.xtext.databinding.internal.EMFValuePropertyWithInvalidFeatureLogging;
import ch.vorburger.xtext.databinding.internal.XtextPropertyListener;


/**
 * Like EMFEditValueProperty, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
@SuppressWarnings({ "unchecked", "restriction" })
public class EMFXtextValueProperty extends EMFValuePropertyWithInvalidFeatureLogging {

	public EMFXtextValueProperty(EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
	}

	@Override
	protected void doSafeSetValue(final Object source, final Object value) {
		if (source instanceof IWriteAccess<?>) {
			IWriteAccess<XtextResource> access = (IWriteAccess<XtextResource>) source;
		    access.modify(new IUnitOfWork.Void<XtextResource>() {
		    	@Override public void process(XtextResource state) throws Exception {
		    		// TODO Handling (via TDD) if it doesn't exist yet! Ideally, don't throw an error, but create it on-the-fly...
		    		EObject eObject = state.getContents().get(0);
		    	    eObject.eSet(EMFXtextValueProperty.this.getFeature(), value);
		    	};
			});
		} else if (source instanceof Resource) {
			// @see below
			Resource resource = (Resource) source;
			EObject eObject = resource.getContents().get(0);
			eObject.eSet(EMFXtextValueProperty.this.getFeature(), value);
		} else {
			throw new IllegalArgumentException("Observed source object is not an IWriteAccess<XtextResource> : " + source);
		}
	}

	@Override
	protected Object doSafeGetValue(final Object source) {
		// @see very similar logic in XtextPropertyListener.getResource() :
		if (source instanceof IReadAccess<?>) {
			IReadAccess<XtextResource> access = (IReadAccess<XtextResource>) source;	
			return access.readOnly(new IUnitOfWork<Object, XtextResource>() {
				@Override public Object exec(XtextResource resource) throws Exception {
		    		// TODO Handling (via TDD) if it doesn't exist yet! Should probably return null and NOT create it on-the-fly?
					EObject eObject = resource.getContents().get(0);
					return EMFXtextValueProperty.super.doSafeGetValue(eObject);
				}
			});
		} else if (source instanceof Resource) {
			// During
			// org.eclipse.core.internal.databinding.observable.masterdetail.DetailObservableValue,
			// when valueFactory() [see below] directly returns the Resource as
			// Observable:
			Resource resource = (Resource) source;
			EObject eObject = resource.getContents().get(0);
			return EMFXtextValueProperty.super.doSafeGetValue(eObject);
		} else {
			throw new IllegalArgumentException("Observed source object is neither an IReadAccess<XtextResource> nor an EMF Resource: " + source);
		}
	}
	
	@Override
	public INativePropertyListener adaptListener(final ISimplePropertyListener listener) {
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
	
	@Override
	public IObservableFactory valueFactory() {
		// NOT return delegate.valueFactory();
		return new IObservableFactory() {
			public IObservable createObservable(Object target) {
				return observe(getResource(target));
			}
		};
	}

	@Override
	public IObservableFactory valueFactory(final Realm realm) {
		// NOT return delegate.valueFactory(realm);
		return new IObservableFactory() {
			public IObservable createObservable(Object target) {
				return observe(realm, getResource(target));
			}
		};
	}

	protected Resource getResource(Object target) {
		EObject eObject = (EObject) target;
		Resource resource = eObject.eResource();
		if (resource != null) {
			return resource;
		} else {
			throw new IllegalArgumentException("EObject has no eResource to observe: " + eObject.toString());
		}
	}

}
