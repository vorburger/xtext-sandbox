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
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.internal.EMFValuePropertyDecorator;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Like EMFProperties, but compatible with Xtext,
 * because it listens to changes on the Resource instead of on the contained EObjects 
 * (which doesn't work with Xtext as the Editor keeps recreating them).
 * 
 * Similar to EMFEditProperties, but based on observing an XTextDocument (IReadAccess<XtextResource>, IWriteAccess<XtextResource>) instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class EMFXtextProperties {

	// TODO Rename *ALL* the classes to drop EMF* prefix, e.g. EMFXtextProperties -> XtextProperties
	
	// TODO It would be better if this class could share some code with EMFProperties..
	// That's not possible today because it's all static - but if one would change it...
	
	public static IEMFValueProperty value(EStructuralFeature feature) {
		IValueProperty property = new EMFXtextValueProperty(feature);
	    EMFValuePropertyDecorator featureProperty = new EMFValuePropertyDecorator(property, feature);
	    return featureProperty;
	}

	public static IEMFValueProperty value(FeaturePath featurePath) {
		throw new UnsupportedOperationException();
	}
	
	// TODO complete this by adding all variations just like in EMFProperties / EMFEditProperties, with FeaturePath/multiple/Set/List/resource stuff etc.

	// TODO old approach IXtextResourceReadWriteAccess arg is wrong here now of course - fix it 
	public static EStructuralFeature value(IXtextResourceReadWriteAccess xTextReadWriteAccess, FeaturePath featurePath) {
		throw new UnsupportedOperationException();
	}

	// TODO old approach IXtextResourceReadWriteAccess arg is wrong here now of course - fix it 
	public static EStructuralFeature set(IXtextResourceReadWriteAccess xTextReadWriteAccess, EStructuralFeature feature) {
		throw new UnsupportedOperationException();
	}
	
}
