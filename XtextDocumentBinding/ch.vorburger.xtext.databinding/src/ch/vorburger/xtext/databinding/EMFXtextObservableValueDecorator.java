/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.internal.EMFObservableValueDecorator;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Like EMFEditObservableValueDecorator, but using an IXtextResourceReadWriteAccess instead of an EditingDomain.
 *
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class EMFXtextObservableValueDecorator extends EMFObservableValueDecorator implements IEMFXtextObservable {

	private final IXtextResourceReadWriteAccess xTextReadWriteAccess;

	public EMFXtextObservableValueDecorator(IXtextResourceReadWriteAccess xTextReadWriteAccess, IObservableValue decorated, EStructuralFeature eStructuralFeature) {
		super(decorated, eStructuralFeature);
		this.xTextReadWriteAccess = xTextReadWriteAccess;
	}

	@Override
	public IXtextResourceReadWriteAccess getReadWriteAccess() {
		return xTextReadWriteAccess;
	}

}
