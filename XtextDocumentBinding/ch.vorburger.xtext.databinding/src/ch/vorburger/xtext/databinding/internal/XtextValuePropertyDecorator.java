/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.internal.databinding.property.ValuePropertyDetailValue;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.IEMFListProperty;
import org.eclipse.emf.databinding.IEMFMapProperty;
import org.eclipse.emf.databinding.IEMFSetProperty;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.internal.EMFValuePropertyDecorator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import ch.vorburger.xtext.databinding.EMFXtextProperties;

/**
 * Like EMFValuePropertyDecorator, but for the Xtext Data Binding.
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class XtextValuePropertyDecorator extends EMFValuePropertyDecorator {

	// TODO Similarly to (EMF)XtextProperties, it would be better if this class could share more code with EMFValuePropertyDecorator...
	// That's not possible today because EMFValuePropertyDecorator uses new instead of some kind of Factory methods - but if one would change that...
	
	// TODO This is a stupid duplication of fields that are already in the parent EMFValuePropertyDecorator, but because they are private and no protected getter, no choice: 
	protected final IValueProperty delegate;
	protected final EStructuralFeature eStructuralFeature;

	public XtextValuePropertyDecorator(IValueProperty delegate, EStructuralFeature eStructuralFeature) {
		super(delegate, eStructuralFeature);
	    this.delegate = delegate;
	    this.eStructuralFeature = eStructuralFeature;
	}
	
	@Override
	public IEMFValueProperty value(EStructuralFeature feature) {
		return value(FeaturePath.fromList(feature));
	}

	@Override
	public IEMFValueProperty value(FeaturePath featurePath) {
		return value(EMFXtextProperties.value(featurePath));
	}

	@Override
	public IEMFValueProperty value(IEMFValueProperty property) {
		return new XtextValuePropertyDecorator(
				new ValuePropertyDetailValue(/*master*/ this, /*detail*/ property), // NOT super.value(property) ! 
				property.getStructuralFeature());
	}
	
	@Override
	public IEMFListProperty list(EStructuralFeature feature) {
		// TODO return list(EMFXtextProperties.list(feature));
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFListProperty list(IEMFListProperty property) {
		// TODO return new XtextListPropertyDecorator(super.list(property), property.getStructuralFeature());
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFSetProperty set(EStructuralFeature feature) {
		// TODO return set(EMFXtextProperties.set(feature));
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFSetProperty set(IEMFSetProperty property) {
		// TODO return new XtextSetPropertyDecorator(super.set(property), property.getStructuralFeature());
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFMapProperty map(EStructuralFeature feature) {
		// TODO return map(XtextProperties.map(feature));
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFMapProperty map(IEMFMapProperty property) {
		// TODO return new XtextMapPropertyDecorator(super.map(property), property.getStructuralFeature());
		throw new UnsupportedOperationException();
	}
	

	
// We might not need Xtext-specific subclassed variants of the Decorators?
//
//
//	@Override
//	public IObservableValue observe(Object source) {
//		// TODO return new XTextObservableValueDecorator(delegate.observe(source), eStructuralFeature);
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public IObservableValue observe(Realm realm, Object source) {
//		// TODO return new XtextObservableValueDecorator(delegate.observe(realm, source), eStructuralFeature);
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public IObservableValue observeDetail(IObservableValue master) {
//		// TODO return new XtextObservableValueDecorator(delegate.observeDetail(master), eStructuralFeature);
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public IObservableList observeDetail(IObservableList master) {
//		// TODO return new XtextObservableListDecorator(delegate.observeDetail(master), eStructuralFeature);
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public IObservableMap observeDetail(IObservableSet master) {
//		// TODO return new XtextObservableMapDecorator(delegate.observeDetail(master), eStructuralFeature);
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public IObservableMap observeDetail(IObservableMap master) {
//		// TODO return new XtextObservableMapDecorator(delegate.observeDetail(master), eStructuralFeature);
//		throw new UnsupportedOperationException();
//	}

}
