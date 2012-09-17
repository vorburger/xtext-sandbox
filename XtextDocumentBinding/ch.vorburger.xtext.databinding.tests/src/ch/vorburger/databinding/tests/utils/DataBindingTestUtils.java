/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.databinding.tests.utils;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.runtime.IStatus;

/**
 * @author Michael Vorburger
 */
public class DataBindingTestUtils {

	/**
	 * Checks Validation Status of all Bindings in a Context.
	 * 
	 * @param context a {@link DataBindingContext}
	 * @throws AssertionError if one (first) of the binding's validation status is not OK 
	 */
	public static void assertContextOK(DataBindingContext context) {
		IObservableList bindings = context.getBindings();
		for (Object object : bindings) {
			Binding binding = (Binding) object;
			assertBindingOK(binding);
		}
	}
	
	/**
	 * Checks Binding Validation Status and throws BindingValidationException if not OK.
	 * 
	 * @param binding a {@link Binding}
	 * @throws AssertionError if the binding's validation status is not OK 
	 */
	public static void assertBindingOK(Binding binding) {
		IStatus status = getValidationStatusCast(binding);
		assertTrue(status.toString(), status.isOK());
	}
	
	private static IStatus getValidationStatusCast(Binding binding) {
		return (IStatus) binding.getValidationStatus().getValue();
	}

}
