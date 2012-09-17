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
import org.eclipse.emf.databinding.internal.EMFValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * EMFValueProperty which logs an error for any Exception caught from eSet() & eGet().
 * 
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=388802 If that suggestion gets implemented, this probably (?) wouldn't be needed anymore, so this is written mostly just because "doppelt genäht hält besser" ;)
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public abstract class EMFValuePropertyWithErrorLogging extends EMFValueProperty {

	public EMFValuePropertyWithErrorLogging(EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
	}

	@Override
	protected Object doGetValue(Object source) {
		try {
			return doSafeGetValue(source);
		} catch (RuntimeException e) {
			Policy.getLog().log(new Status(IStatus.ERROR, Policy.JFACE_DATABINDING, 0, "Error in EMF eGet", e)); //$NON-NLS-1$
			throw e;
		}
	}

	abstract protected Object doSafeGetValue(Object source);

	@Override
	protected void doSetValue(Object source, Object value) {
		try {
			doSafeSetValue(source, value);
		} catch (RuntimeException e) {
			Policy.getLog().log(new Status(IStatus.ERROR, Policy.JFACE_DATABINDING, 0, "Error in EMF eSet", e)); //$NON-NLS-1$
			throw e;
		}
	}

	abstract protected void doSafeSetValue(Object source, Object value);

}
