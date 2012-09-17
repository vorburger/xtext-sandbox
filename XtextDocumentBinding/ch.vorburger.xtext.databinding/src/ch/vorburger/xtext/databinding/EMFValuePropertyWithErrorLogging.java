/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.util.Policy;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.databinding.internal.EMFValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ExtendedMetaData;

/**
 * EMFValueProperty which logs an error for any Exception caught from eSet() & eGet().
 * 
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=388802 If that suggestion gets implemented, this probably (?) wouldn't be needed anymore, so this is written mostly just because "doppelt genäht hält besser" ;)
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class EMFValuePropertyWithErrorLogging extends EMFValueProperty {

	public EMFValuePropertyWithErrorLogging(EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
	}

	@Override
	protected Object doGetValue(Object source) {
		try {
			// return super.doGetValue(source);
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
		} catch (RuntimeException e) {
			Policy.getLog().log(new Status(IStatus.ERROR, Policy.JFACE_DATABINDING, 0, "Error in EMF eGet", e)); //$NON-NLS-1$
			throw e;
		}
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		try {
			super.doSetValue(source, value);
		} catch (RuntimeException e) {
			Policy.getLog().log(new Status(IStatus.ERROR, Policy.JFACE_DATABINDING, 0, "Error in EMF eSet", e)); //$NON-NLS-1$
			throw e;
		}
	}

}
