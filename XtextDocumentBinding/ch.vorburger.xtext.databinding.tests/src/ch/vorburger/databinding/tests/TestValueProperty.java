/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

/**
 *  Copyright 2012 Michael Vorburger (http://www.vorburger.ch)
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ch.vorburger.databinding.tests;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;

/**
 * IValueProperty for Tests.
 * Typically used with the SimplePropertyObservableValue IObservableValue. 
 * 
 * @author Michael Vorburger
 */
public class TestValueProperty extends SimpleValueProperty {

	private Object newValue;

	@Override
	protected Object doGetValue(Object source) {
		return newValue;
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		newValue = value;
	}

	@Override
	public INativePropertyListener adaptListener(ISimplePropertyListener listener) {
		return new TestPropertyListener(this, listener);
	}

	@Override
	public Object getValueType() {
		return String.class;
	}

}
