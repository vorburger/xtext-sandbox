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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.databinding.EMFUpdateListStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.vorburger.beans.AbstractPropertyChangeNotifier;
import ch.vorburger.databinding.tests.utils.DataBindingTestUtils;
import ch.vorburger.databinding.tests.utils.DatabindingTestRealm;
import ch.vorburger.xtext.databinding.EMFXtextProperties;
import ch.vorburger.xtext.databinding.XtextDataBindingContext;
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
		private List<Bean> list = new ArrayList<EMFXtextPropertiesTest.Bean>();
		public void setName(String name) {
			firePropertyChange("name", this.name, this.name = name);
		}
		public String getName() {
			return name;
		}
		public List<Bean> getList() {
			return list;
		}
		@SuppressWarnings("unused") // BeanProperties requires this 
		public void setList(List<Bean> list) {
			this.list = list;
		}
	}

	private XtextDataBindingContext db;
	private EAttribute titleFeature;
	private EReference referenceFeature;
	private EReference listFeature;
	private Bean bean;
	private XtextResourceTestAccess access;
	private EObject eObject;
	private EObject containedEObject;
	private EClass clazz;
	private ECoreHelper helper;

	@Before
	public void setUp() {
		// Create an ECore model
		helper = new ECoreHelper();
		EDataType stringType = EcorePackage.eINSTANCE.getEString();
		EPackage pkg = helper.createPackage("tests");
		clazz = helper.createClass(pkg, "Test");
		titleFeature = helper.addAttribute(clazz, stringType, "title");
		referenceFeature = helper.addContainmentReference(clazz, clazz, "childContainmentReferenceToTest");
		listFeature = helper.addMultiContainmentReference(clazz, clazz, "list");
		
		// Create EObjects
		eObject = helper.createInstance(clazz);
		eObject.eSet(titleFeature, "This is the Title");

		containedEObject = helper.createInstance(clazz);
		eObject.eSet(referenceFeature, containedEObject);
		
		// Create a Bean
		bean = new Bean();
		
		Realm realm = new DatabindingTestRealm();
		db = new XtextDataBindingContext(realm);
		
		// TODO HIGH Use an indirect WritableValue in observe instead of direct (both for illustration and to test SourceAccessor stuff)
		
		XtextResource resource = new XtextResource();
		resource.getContents().add(eObject);
		access = new XtextResourceTestAccess(resource);
	}

	@Test
	public void testURIFragment() {
		Resource resource = containedEObject.eResource();
		String uriFragment = resource.getURIFragment(containedEObject);
		EObject gotEObject = resource.getEObject(uriFragment);
		assertEquals(containedEObject, gotEObject);
		// System.out.println(resource.getURIFragment(eObject));
		// System.out.println(uriFragment);
	}
	
	@Test
	public void testSimpleValueBinding() {
		db.bindValue(BeanProperties.value("name").observe(bean),
				EMFXtextProperties.value(titleFeature).observe(access));
		DataBindingTestUtils.assertContextOK(db);
		
		assertEquals(bean.getName(), eObject.eGet(titleFeature));

		// First let's test changing the target and checking the model
		bean.setName("reset, reset");
		assertEquals("reset, reset", bean.getName()); // This just tests the bean, not the binding
		assertEquals("reset, reset", eObject.eGet(titleFeature));
		
		// Now let's test changing the model and checking the target
		// Start by changing another feature of the model (which is not data bound, in this test)
		eObject.eSet(referenceFeature, null);
		// Of course, the other feature (under test) should, obviously, not change yet
		assertEquals("reset, reset", bean.getName());
		// Now we change the data bound feature in model and make sure the target changed 
		eObject.eSet(titleFeature, "setitsetit!");
		assertEquals("setitsetit!", bean.getName());
		
		DataBindingTestUtils.assertContextOK(db);
	}

	/**
	 * Tests that using an EObject in observe(), like the EMFProperties Data Binding API expects fails.
	 * The XtextProperties Data Binding API needs to be observing an XTextDocument (IReadAccess<XtextResource>, IWriteAccess<XtextResource>). 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testErrorObserveObjectInsteadOfResourceAcess() {
		db.bindValue(
				BeanProperties.value("name").observe(bean),
				EMFXtextProperties.value(titleFeature).observe(eObject));
	}

	@Test
	public void testPathFeatureBinding() {
		db.bindValue(BeanProperties.value("name").observe(bean),
				EMFXtextProperties.value(FeaturePath.fromList(referenceFeature, titleFeature)).observe(access));
		DataBindingTestUtils.assertContextOK(db);
		
		// Referenced Feature has now been set (sync) by binding:
		assertEquals(bean.getName(), ((EObject)eObject.eGet(referenceFeature)).eGet(titleFeature));
		// Root object feature is as set in setUp():
		assertEquals("This is the Title", eObject.eGet(titleFeature));

		// Again, first let's test changing the target and checking the model
		bean.setName("reset, reset");
		// Root object feature should NOT have changed:
		assertEquals("This is the Title", eObject.eGet(titleFeature));
		// Referenced Feature has now been set (sync) by binding and changed:
		assertEquals("reset, reset", ((EObject)eObject.eGet(referenceFeature)).eGet(titleFeature));
		
		// Now let's test changing the model and checking the target
		// Start by changing another feature of the model (which is not data bound, in this test)
		containedEObject.eSet(referenceFeature, helper.createInstance(clazz));
		// Of course, the other feature (under test) should, obviously, not change yet
		assertEquals("reset, reset", bean.getName());
		// Now we change the data bound feature in model and make sure the target changed 
		containedEObject.eSet(titleFeature, "setitsetit!");
		assertEquals("setitsetit!", bean.getName());
		
		DataBindingTestUtils.assertContextOK(db);
	}

	// TODO HIGH referenceFeature in path was initially null and must be constructed during sync!
	
	@Test
	@SuppressWarnings("unchecked")
	public void testListBinding() {
		EObject eObjectInList = helper.createInstance(clazz);
		eObjectInList.eSet(titleFeature, "First Object in List");
		List<EObject> list = (List<EObject>) eObject.eGet(listFeature);
		list.add(eObjectInList);
		
		UpdateListStrategy modelToTarget = new EMFUpdateListStrategy() {
			@Override
			protected IConverter createConverter(Object fromType, Object toType) {
				// TODO Remove System.out.println - or better move it into a else Log WARN below? 
				System.out.println(getClass().getName() + " :: createConverter :" + fromType.toString() + " -> " + toType.toString());
				if (fromType instanceof EObject /* TODO && toType instanceof Bean */) {
					// TODO It should be possible to write this in a generic fashion? Backed by Binding?
					return new Converter(fromType, toType) {
						@Override
						public Object convert(Object fromObject) {
							final Bean newBean = new Bean();
							EObject eObject = (EObject) fromObject;
							newBean.setName((String) eObject.eGet(titleFeature));
							return newBean;
						}
					};
				}
				return super.createConverter(fromType, toType);
			}
		};
		db.bindList(BeanProperties.list("list").observe(bean),
				EMFXtextProperties.list(listFeature).observe(access),
				null, modelToTarget);
		DataBindingTestUtils.assertContextOK(db);
		
		// Check that an object was added to the bound list
		List<Bean> beanList = bean.getList();
		assertEquals(1, beanList.size());
		Bean firstBeanInList = beanList.get(0);
		assertEquals("First Object in List", firstBeanInList.getName());
		
		// Check that a modification of the object in the list is sync'd
		eObjectInList.eSet(titleFeature, "First Object in List CHANGED");
		assertEquals("First Object in List CHANGED", firstBeanInList.getName());
		
		// Check opposite modification of the object in the list is sync'd
		firstBeanInList.setName("First BEAN in List");
		assertEquals("First BEAN in List", eObjectInList.eGet(titleFeature));
	}

	// TODO HIGH List Binding change position 
	
	@After
	public void tearDown() {
		db.dispose();
	}

}
