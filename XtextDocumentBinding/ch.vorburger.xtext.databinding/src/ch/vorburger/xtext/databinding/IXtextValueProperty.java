/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

/**
 * Like IEMFValueProperty, but using an IXtextResourceReadWriteAccess instead of an direct EObject access (or an EditingDomain).
 *
 * @author Michael Vorburger
 */
public interface IXtextValueProperty extends IEMFValueProperty {
	// TODO HIGH Extract IXtextProperty super interface, used also by IXtextListProperty
	
	// TODO LOW Change argument type from IWriteAccess<XtextResource> to IXtextResourceReadWriteAccess once XTextDocument implements that
	IObservableValue observe(IWriteAccess<XtextResource> source);

	// TODO LOW Change argument type from IWriteAccess<XtextResource> to IXtextResourceReadWriteAccess once XTextDocument implements that
	IObservableValue observe(Realm realm, IWriteAccess<XtextResource> source);

	IXtextValueProperty value(EStructuralFeature feature);
	
	// TODO HIGH needed? IXtextValueProperty value(IXtextValueProperty property);

	// TODO HIGH IXtextValueProperty list(IEMFXtextListProperty property);

	// TODO LOW IXtextValueProperty map(IEMFXtextMapProperty property);
	
}
