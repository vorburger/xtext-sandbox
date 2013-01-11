/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Functions;
import org.eclipse.xtext.xbase.lib.Procedures;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.junit.Test;

import ch.vorburger.xbindings.XBinding;
import ch.vorburger.xbindings.property.Property;
import ch.vorburger.xbindings.property.PropertyImpl;
import ch.vorburger.xbindings.list.XListBinding;

/**
 * XBindings Test (and example) for Lists, written in Java (without lambda expressions / closures).
 * 
 * @author Michael Vorburger
 */
public class XBindingsListsJavaTest {

	// TODO write nicer Xtend variant of the same
	
	@Test
	public void testListBinding() {
		List<Property<String>> aList = new ArrayList<Property<String>>(); 
		List<Property<String>> bList = new ArrayList<Property<String>>();

		// TODO how to wrap/pass aList / bList?
		new XListBinding(new Functions.Function0<Property<String>>() {
			@Override
			public Property<String> apply() {
				return new PropertyImpl<>();
			}
		}, new Procedures.Procedure2<Property<String>, Property<String>>() {
			// TODO combine this instead with XBinding (after refactoring..)
			@Override
			public void apply(Property<String> in, Property<String> out) {
				out.set("Hey " + in.get());
				
			}
		});
		
		Property<String> a1 = new PropertyImpl<String>("Juhu");
		aList.add(a1);
		assertEquals(1, bList.size());
		assertEquals("Hey Juhu", bList.get(0).get());
		
		a1.set("Tata");
		assertEquals("Hey Tata", bList.get(0).get());
		
		aList.remove(0);
		assertEquals(0, bList.size());
	}

}
