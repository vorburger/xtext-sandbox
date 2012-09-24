/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.internal.databinding.property.ValuePropertyDetailValue;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.IEMFListProperty;
import org.eclipse.emf.databinding.IEMFMapProperty;
import org.eclipse.emf.databinding.IEMFSetProperty;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.internal.EMFValuePropertyDecorator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

import ch.vorburger.xtext.databinding.XtextProperties;
import ch.vorburger.xtext.databinding.IXtextValueProperty;
import ch.vorburger.xtext.databinding.internal.sourceadapt.SourceAccessor;
import ch.vorburger.xtext.databinding.internal.sourceadapt.XTextDocumentSourceAccessor;
import ch.vorburger.xtext.databinding.internal.sourceadapt.XtextResourceDelegatingAccess;

/**
 * Like EMFValuePropertyDecorator, but for the Xtext Data Binding.
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class XtextValuePropertyDecorator extends EMFValuePropertyDecorator implements IXtextValueProperty {

	// TODO MEDIUM EXPLORE Might be able & better to do use the same pattern as EMF Edit DB instead of what what I did via SourceAccessor, by introducing a XtextObservableValueDecorator, à la EMFEditObservableValueDecorator?
	// But then this class would have to have an IWriteAccess/IXtextResourceReadWriteAccess field,
	// just like EMFEditValuePropertyDecorator has an EditingDomain... which is not what we want - as that should be the source. How to unify these two concepts nicely? 

	// TODO LOW Similarly to (EMF)XtextProperties, it would be better if this class could share more code with EMFValuePropertyDecorator...
	// That's not possible today because EMFValuePropertyDecorator uses new instead of some kind of Factory methods - but if one would change that...
	// NOTE EMFEditValuePropertyDecorator actually has the same problem.. it extends ValueProperty instead of EMFValuePropertyDecorator 
	
	// See above; this is a stupid duplication of fields that are already in the parent EMFValuePropertyDecorator, but because they are private and no protected getter, no choice: 
	protected final IValueProperty delegate;
	protected final EStructuralFeature eStructuralFeature;

	public XtextValuePropertyDecorator(IValueProperty delegate, EStructuralFeature eStructuralFeature) {
		super(delegate, eStructuralFeature);
	    this.delegate = delegate;
	    this.eStructuralFeature = eStructuralFeature;
	}

	@Override
	@SuppressWarnings("unchecked")
	public IObservableValue observe(Object source) {
		if (source instanceof SourceAccessor) {
			return observe((SourceAccessor) source);
		} else if (source instanceof IWriteAccess<?>) {
			return observe((IWriteAccess<XtextResource>) source);
		} else {
			throw new IllegalArgumentException("source object to observe is not an IWriteAccess<XtextResource> (nor a SourceAccessor already): " + source);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public IObservableValue observe(Realm realm, Object source) {
		if (source instanceof SourceAccessor) {
			return observe(realm, (SourceAccessor) source);
		} else if (source instanceof IWriteAccess<?>) {
			return observe(realm, (IWriteAccess<XtextResource>) source);
		} else {
			throw new IllegalArgumentException("source object to observe is not an IWriteAccess<XtextResource> (nor a SourceAccessor already): " + source);
		}
	}

	// NOTE: These two methods invoking just super() may appear useless at
	// first, but they are not - remove them and watch the StackOverflowError
	// and you'll understand... ;-)
	//
	protected IObservableValue observe(SourceAccessor source) {
		return super.observe(source);
	}
	protected IObservableValue observe(Realm realm, SourceAccessor source) {
		return super.observe(realm, source);
	}

	@Override
	// TODO LOW Change argument type from IWriteAccess<XtextResource> to IXtextResourceReadWriteAccess once XTextDocument implements that
	public IObservableValue observe(IWriteAccess<XtextResource> source) {
		// // TODO LOW Remove gimmick once XTextDocument implements IXtextResourceReadWriteAccess
		XtextResourceDelegatingAccess gimmick = new XtextResourceDelegatingAccess(source);
		SourceAccessor wrappedSource = new XTextDocumentSourceAccessor(gimmick);
		return observe(wrappedSource);
	}

	@Override
	// TODO LOW Change argument type from IWriteAccess<XtextResource> to IXtextResourceReadWriteAccess once XTextDocument implements that
	public IObservableValue observe(Realm realm, IWriteAccess<XtextResource> source) {
		// TODO LOW Remove gimmick once XTextDocument implements IXtextResourceReadWriteAccess
		XtextResourceDelegatingAccess gimmick = new XtextResourceDelegatingAccess(source);
		SourceAccessor wrappedSource = new XTextDocumentSourceAccessor(gimmick);
		return observe(realm, wrappedSource);
	}
	
	@Override
	public IXtextValueProperty value(EStructuralFeature feature) {
		return value(FeaturePath.fromList(feature));
	}

	@Override
	public IXtextValueProperty value(FeaturePath featurePath) {
		return value(XtextProperties.value(featurePath));
	}

	@Override
	public IXtextValueProperty value(IEMFValueProperty property) {
		return new XtextValuePropertyDecorator(
				new ValuePropertyDetailValue(/*master*/ this, /*detail*/ property), // NOT super.value(property) ! 
				property.getStructuralFeature());
	}
	
	@Override
	public IEMFListProperty list(EStructuralFeature feature) {
		// TODO HIGH return list(XtextProperties.list(feature));
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFListProperty list(IEMFListProperty property) {
		// TODO HIGH return new XtextListPropertyDecorator(super.list(property), property.getStructuralFeature());
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFSetProperty set(EStructuralFeature feature) {
		// TODO LOW return set(XtextProperties.set(feature));
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFSetProperty set(IEMFSetProperty property) {
		// TODO LOW return new XtextSetPropertyDecorator(super.set(property), property.getStructuralFeature());
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFMapProperty map(EStructuralFeature feature) {
		// TODO LOW return map(XtextProperties.map(feature));
		throw new UnsupportedOperationException();
	}

	@Override
	public IEMFMapProperty map(IEMFMapProperty property) {
		// TODO LOW return new XtextMapPropertyDecorator(super.map(property), property.getStructuralFeature());
		throw new UnsupportedOperationException();
	}

}
