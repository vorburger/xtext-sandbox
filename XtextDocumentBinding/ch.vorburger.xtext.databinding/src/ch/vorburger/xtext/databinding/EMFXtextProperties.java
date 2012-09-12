/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

/**
 * Like EMFEditProperties, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
public class EMFXtextProperties {

	public static IValueProperty value(IWriteAccess<XtextResource> xTextWriteAccess, EStructuralFeature feature) {
		IXtextResourceReadWriteAccess xTextReadWriteAccess = new XtextResourceDelegatingAccess(xTextWriteAccess);
				
		IValueProperty property = new EMFXtextValueProperty(xTextReadWriteAccess, feature);
	    IEMFXtextValueProperty featureProperty = new EMFXtextValuePropertyDecorator(xTextReadWriteAccess, property, feature);
	    return featureProperty;
	}

	public static EStructuralFeature set(IXtextResourceReadWriteAccess xTextReadWriteAccess, EStructuralFeature feature) {
		throw new UnsupportedOperationException();
	}

	public static EStructuralFeature value(IXtextResourceReadWriteAccess xTextReadWriteAccess, FeaturePath featurePath) {
		throw new UnsupportedOperationException();
	}

	// TODO all variations just like in EMFEditProperties, with FeaturePath/multiple/Set/List/resource stuff etc.
	
}
