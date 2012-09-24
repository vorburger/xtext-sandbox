/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.observable.IDecoratingObservable;
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

import ch.vorburger.xtext.databinding.internal.XtextPropertyListener;
import ch.vorburger.xtext.databinding.internal.nicetohave.EMFValuePropertyWithInvalidFeatureLogging;
import ch.vorburger.xtext.databinding.internal.sourceadapt.SourceAccessor;
import ch.vorburger.xtext.databinding.internal.sourceadapt.XTextDocumentSourceAccessor;


/**
 * Like EMFEditValueProperty, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class XtextValueProperty extends EMFValuePropertyWithInvalidFeatureLogging {

	public XtextValueProperty(EStructuralFeature eStructuralFeature) {
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
				return XtextValueProperty.this;
			}

			@Override
			protected ISimplePropertyListener getListener() {
				return listener;
			}

			@Override
			protected EStructuralFeature getFeature() {
				return XtextValueProperty.this.getFeature();
			}
		};
	}

	@Override
	public IObservableValue observeDetail(IObservableValue master) {
		return MasterDetailObservables.detailValue(master, valueFactory(master), getValueType());
	}

// TODO HIGH needed?!
//	
//	@Override
//	public IObservableList observeDetail(IObservableList master) {
//		return MasterDetailObservables.detailValues(master, valueFactory(master), getValueType());
//	}
//
//	@Override
//	public IObservableMap observeDetail(IObservableSet master) {
//		return MasterDetailObservables.detailValues(master, valueFactory(master), getValueType());
//	}
//
//	@Override
//	public IObservableMap observeDetail(IObservableMap master) {
//		return MasterDetailObservables.detailValues(master, valueFactory(master), getValueType());
//	}

	private IObservableFactory valueFactory(final IObservable master) {
		final IObserving observing = getObserving(master);
		return new IObservableFactory() {
			public IObservable createObservable(Object target) {
				Object observed = observing.getObserved();
				XTextDocumentSourceAccessor masterAccessor = getSourceAccessor(observed);
				EObject eObject = (EObject) target;
				XTextDocumentSourceAccessor source = new XTextDocumentSourceAccessor(masterAccessor, eObject);
				return observe(master.getRealm(), source);
			}
		};
	}

	protected XTextDocumentSourceAccessor getSourceAccessor(final Object observed) throws IllegalArgumentException {
		if (observed instanceof XTextDocumentSourceAccessor) {
			return (XTextDocumentSourceAccessor) observed;
		} else {
			throw new IllegalArgumentException("IObservable master is an IObserving, but not an XTextDocumentSourceAccessor: " + observed);
		}
	}
	
	protected IObserving getObserving(final IObservable master) throws IllegalArgumentException {
		if (master instanceof IObserving) {
			return (IObserving) master;
		} else if (master instanceof IDecoratingObservable) {
			IDecoratingObservable decorating = (IDecoratingObservable) master;
			IObservable decorated = decorating.getDecorated();
			return getObserving(decorated);
		} else {
			throw new IllegalArgumentException("IObservable master is not an IObserving nor an IDecoratingObservable: " + master); 
		}
	}

	@Override
	public IObservableFactory valueFactory() {
		throw new UnsupportedOperationException("Knock, knock - who's calling? ;) You should go through something like the overloaded observeDetail() ...");
	}
	
	@Override
	public IObservableFactory valueFactory(final Realm realm) {
		throw new UnsupportedOperationException("Knock, knock - who's calling? ;) You should go through something like the overloaded observeDetail() ...");
	}
}
