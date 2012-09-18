/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.masterdetail.MasterDetailObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import ch.vorburger.xtext.databinding.internal.EMFValuePropertyWithInvalidFeatureLogging;
import ch.vorburger.xtext.databinding.internal.XtextPropertyListener;
import ch.vorburger.xtext.databinding.internal.sourceadapt.SourceAccessor;
import ch.vorburger.xtext.databinding.internal.sourceadapt.XTextDocumentSourceAccessor;


/**
 * Like EMFEditValueProperty, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class EMFXtextValueProperty extends EMFValuePropertyWithInvalidFeatureLogging {

	public EMFXtextValueProperty(EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
	}

	@Override
	protected void doSafeSetValue(final Object source, final Object value) {
		SourceAccessor sourceAccessor = (SourceAccessor) source;
		sourceAccessor.eSet(getFeature(), value);
	}

	@Override
	protected Object doSafeGetValue(final Object source) {
		SourceAccessor sourceAccessor = (SourceAccessor) source;
		return sourceAccessor.eGet(getFeature());
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

	// TODO Doc why are we doing this:
	// During
	// org.eclipse.core.internal.databinding.observable.masterdetail.DetailObservableValue,
	// when valueFactory() [see below] directly returns the Resource as
	// Observable:

	@Override
	public IObservableValue observeDetail(IObservableValue master) {
		return MasterDetailObservables.detailValue(master, 
				valueFactory(master), getValueType());
	}

	private IObservableFactory valueFactory(final IObservableValue master) {
		return new IObservableFactory() {
			public IObservable createObservable(Object target) {
				return observe(master.getRealm(), getSourceAccessorWrapper(master, target));
			}
		};
	}

	@Override
	public IObservableFactory valueFactory() {
		throw new UnsupportedOperationException("Knock, knock - who's calling? ;) Should go through something overloaded observeDetail() ...");
//		return new IObservableFactory() {
//			public IObservable createObservable(Object target) {
//				return observe(getSourceAccessorWrapper(target));
//			}
//		};
	}
	@Override
	public IObservableFactory valueFactory(final Realm realm) {
		throw new UnsupportedOperationException("Knock, knock - who's calling? ;) Should go through something overloaded observeDetail() ...");
//		return new IObservableFactory() {
//			public IObservable createObservable(Object target) {
//				return observe(realm, getSourceAccessorWrapper(target));
//			}
//		};
	}

	protected Object getSourceAccessorWrapper(IObservableValue master, Object target) {
		if (master instanceof IObserving) {
			IObserving observing = (IObserving) master;
			Object observed = observing.getObserved();
			if (observed instanceof XTextDocumentSourceAccessor) {
				XTextDocumentSourceAccessor masterAccessor = (XTextDocumentSourceAccessor) observed;
				EObject eObject = (EObject) target;
				return new XTextDocumentSourceAccessor(masterAccessor, eObject);
			} else
				throw new IllegalArgumentException("IObservableValue master is an IObserving, but not an XTextDocumentSourceAccessor: " + master); 
		} else
			throw new IllegalArgumentException("IObservableValue master is not an IObserving: " + master); 
	}

}
