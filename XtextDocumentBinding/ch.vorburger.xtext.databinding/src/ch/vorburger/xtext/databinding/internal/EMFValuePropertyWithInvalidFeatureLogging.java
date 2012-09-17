/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal;

import org.eclipse.core.databinding.util.Policy;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;

/**
 * EMFValueProperty which logs when getting an invalid feature (instead of just returning null).
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public abstract class EMFValuePropertyWithInvalidFeatureLogging extends EMFValuePropertyWithErrorLogging {

	public EMFValuePropertyWithInvalidFeatureLogging(EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
	}

	@Override
	protected Object doSafeGetValue(Object source) {
		// From super.doGetValue(source) :
	    EObject eObj = (EObject)source;
	    if (ExtendedMetaData.INSTANCE.getAffiliation(eObj.eClass(), getFeature()) != null) {
	    	return eObj.eGet(getFeature());
	    } else {
	    	String msg = "Could not find Feature '" + getFeature().getName()
	    			+ "' of EClass '" + getFeature().getEContainingClass().getName()
	    			+ "' on EObject " + eObj.toString(); //$NON-NLS-1$
			Policy.getLog().log(new Status(IStatus.ERROR, Policy.JFACE_DATABINDING, msg));
			throw new IllegalArgumentException(msg);
	    }
	}

}
