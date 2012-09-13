/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.databinding.property.value.ValueProperty;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.IEMFListProperty;
import org.eclipse.emf.databinding.IEMFMapProperty;
import org.eclipse.emf.databinding.IEMFSetProperty;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Like EMFEditValuePropertyDecorator, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
public class EMFXtextValuePropertyDecorator extends ValueProperty implements IEMFXtextValueProperty {

	// TODO Revisit later if this is still needed... since EMFXtextProperties switched to EMFValuePropertyDecorator ?
	
	private final IValueProperty delegate;
	private final EStructuralFeature eStructuralFeature;
	private final IXtextResourceReadWriteAccess xTextReadWriteAccess;

	public EMFXtextValuePropertyDecorator(IXtextResourceReadWriteAccess xTextReadWriteAccess, IValueProperty delegate, EStructuralFeature eStructuralFeature) {
		this.delegate = delegate;
	    this.eStructuralFeature = eStructuralFeature;
		this.xTextReadWriteAccess = xTextReadWriteAccess;
	}

	@Override
	public IXtextResourceReadWriteAccess getReadWriteAccess() {
		return xTextReadWriteAccess;
	}

	@Override
	public IEMFValueProperty value(EStructuralFeature feature) {
	    return value(FeaturePath.fromList(feature));
	}

	@Override
	public IEMFValueProperty value(FeaturePath featurePath) {
	    return value(EMFXtextProperties.value(xTextReadWriteAccess, featurePath));
	}

	@Override
	public IEMFValueProperty value(IEMFValueProperty property) {
	    return new EMFXtextValuePropertyDecorator(xTextReadWriteAccess, super.value(property), property.getStructuralFeature());
	}

	@Override
	public IEMFXtextValueProperty value(IEMFXtextValueProperty property) {
	    return new EMFXtextValuePropertyDecorator(xTextReadWriteAccess, super.value(property), property.getStructuralFeature());
	}

	@Override
	public IEMFListProperty list(EStructuralFeature feature) {
		throw new UnsupportedOperationException();
		// TODO Auto-generated method stub
	}

	@Override
	public IEMFListProperty list(IEMFListProperty property) {
		throw new UnsupportedOperationException();
		// TODO Auto-generated method stub
	}

	@Override
	public IEMFXtextListProperty list(IEMFXtextListProperty property) {
		throw new UnsupportedOperationException();
		// TODO Auto-generated method stub
	}

	@Override
	public IObservableFactory valueFactory() {
		return delegate.valueFactory();
	}

	@Override
	public IObservableFactory valueFactory(Realm realm) {
		return delegate.valueFactory(realm);
	}

	@Override
	public IObservableValue observeDetail(IObservableValue master) {
	    return new EMFXtextObservableValueDecorator(xTextReadWriteAccess, delegate.observeDetail(master), eStructuralFeature);
	}

	@Override
	public IObservableList observeDetail(IObservableList master) {
		throw new UnsupportedOperationException();
	    // TODO return new EMFXtextObservableListDecorator(xTextReadWriteAccess, delegate.observeDetail(master), eStructuralFeature);
	}

	@Override
	public IObservableMap observeDetail(IObservableSet master) {
		throw new UnsupportedOperationException();
	    // TODO return new EMFXtextObservableMapDecorator(xTextReadWriteAccess, delegate.observeDetail(master), eStructuralFeature);
	}

	@Override
	public IObservableMap observeDetail(IObservableMap master) {
		throw new UnsupportedOperationException();
	    // TODO return new EMFXtextObservableMapDecorator(xTextReadWriteAccess, delegate.observeDetail(master), eStructuralFeature);
	}

	@Override
	public IEMFMapProperty map(EStructuralFeature feature) {
		throw new UnsupportedOperationException();
	    // TODO return EMFXtextProperties.map(xTextReadWriteAccess, feature);
	}

	@Override
	public IEMFMapProperty map(IEMFMapProperty property) {
		throw new UnsupportedOperationException();
	    // TODO return new EMFXtextMapPropertyDecorator(xTextReadWriteAccess, super.map(property), property.getStructuralFeature());
	}

	@Override
	public IEMFXtextMapProperty map(IEMFXtextMapProperty property) {
		throw new UnsupportedOperationException();
		// TODO
	}

	@Override
	public IEMFSetProperty set(EStructuralFeature feature) {
	    return set(EMFXtextProperties.set(xTextReadWriteAccess, feature));
	}

	@Override
	public IEMFSetProperty set(IEMFSetProperty property) {
		throw new UnsupportedOperationException();
	    // TODO return new EMFXtextSetPropertyDecorator(xTextReadWriteAccess, super.set(property), property.getStructuralFeature());
	}

	@Override
	public EStructuralFeature getStructuralFeature() {
		return eStructuralFeature;
	}

	@Override
	public Object getValueType() {
		return delegate.getValueType();
	}

	@Override
	public IObservableValue observe(Realm realm, Object source) {
	    return new EMFXtextObservableValueDecorator(xTextReadWriteAccess, delegate.observe(realm, source), eStructuralFeature);
	}

	@Override
	public IObservableValue observe(Object source) {
	    return new EMFXtextObservableValueDecorator(xTextReadWriteAccess, delegate.observe(source), eStructuralFeature);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
