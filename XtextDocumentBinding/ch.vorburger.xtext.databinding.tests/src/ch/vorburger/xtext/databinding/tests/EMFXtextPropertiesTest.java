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

package ch.vorburger.xtext.databinding.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.resource.XtextResource;
import org.junit.Test;

import ch.vorburger.beans.AbstractPropertyChangeNotifier;
import ch.vorburger.xtext.databinding.EMFXtextProperties;
import ch.vorburger.xtext.databinding.XtextDataBindingContext;
import ch.vorburger.xtext.databinding.tests.utils.DatabindingTestRealm;
import ch.vorburger.xtext.databinding.tests.utils.ECoreHelper;
import ch.vorburger.xtext.databinding.tests.utils.XtextResourceTestAccess;

/**
 * Tests for the XtexteDataBindingContext & XtextProperties.
 * 
 * @author Michael Vorburger
 */
public class EMFXtextPropertiesTest {

	@SuppressWarnings("serial")
	private static class Bean extends AbstractPropertyChangeNotifier {
		private String name;
		public void setName(String name) {
			firePropertyChange("name", this.name, this.name = name);
		}
		public String getName() {
			return name;
		}
	}
	
	@Test
	public void testSimpleValueBinding() {
		// Create an ECore model
		ECoreHelper helper = new ECoreHelper();
		EDataType stringType = EcorePackage.eINSTANCE.getEString();
		EPackage pkg = helper.createPackage("tests");
		EClass clazz = helper.createClass(pkg, "Test");
		EAttribute titleFeature = helper.addAttribute(clazz, stringType, "title");

		// Create an EObject
		EObject eObject = helper.createInstance(clazz);
		eObject.eSet(titleFeature, "This is the Title");
		
		// Create a Bean
		Bean bean = new Bean();
		
		Realm realm = new DatabindingTestRealm();
		DataBindingContext db = new XtextDataBindingContext(realm);
		
		// TODO Use an indirect WritableValue in observe instead of direct (both for illustration and to test it)
		
		// TODO Do I really need to go through an XtextResource? Better abstraction?
		XtextResource resource = new XtextResource();
		resource.getContents().add(eObject);
		XtextResourceTestAccess access = new XtextResourceTestAccess(resource);
		db.bindValue(BeanProperties.value("name").observe(bean),
				EMFXtextProperties.value(titleFeature).observe(access));
		
		assertEquals(eObject.eGet(titleFeature), bean.getName());
		
		bean.setName("reset, reset");
		assertEquals("reset, reset", bean.getName());
		assertEquals("reset, reset", eObject.eGet(titleFeature));

		db.dispose();
	}

}
