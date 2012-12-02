/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings

import org.junit.Test
import static org.junit.Assert.*
import static extension ch.vorburger.xbindings.XBindings.*
import org.junit.Ignore

/**
 * XBindings Test (and example) written in Xtend.
 * 
 * @see http://www.eclipse.org/xtend/
 * @author Michael Vorburger
 */
class XBindingsXtendTest {
	
	val Property<String> aName = new PropertyImpl<String>();
	val Property<String> bName = new PropertyImpl<String>();

	@Test def testOneWayBinding() {
		aName <= "hello"                     // sugar for: aName.set("hello");
		
		bName <= aName                       // creating a data binding
		assertEquals("hello", bName.get());  // obviously! (initial value)
		assertEquals("hello", aName.get());  // just for non-regression of a bug I had initially
		
		aName <= "world"
		assertEquals("world", bName.get());  // wow - magic! ;)
	}

	@Test def testOtherBinding() {
		aName <= "hello"

		// same as testOneWayBinding just expressed the other way around
		aName => bName		
		assertEquals("hello", bName.get());
		
		aName <= "world"
		assertEquals("world", bName.get());
	}

	@Ignore // TODO activate when loop handling is implemented (or inherited from EDB)
	@Test def testBidiBinding() {
		aName <= "hello"
		
		// The <=> syntax creates a bidirectional data binding
		bName <=> aName                       
		assertEquals("hello", bName.get());
		
		aName <= "world"
		assertEquals("world", bName.get());
		
		bName <= "yo"
		assertEquals("yo", aName.get());
	}
	
	@Test def testComputedValue() {
		aName.set("world");
		
		// any expression can go into bind - simple concatenation, or more complex with conversion and condition
		bind[| bName.set("hello, " + aName.get) ]
		assertEquals("hello, world", bName.get());
		
		aName.set("Mondo");
		assertEquals("hello, Mondo", bName.get());
	}

	@Test def testComputedValueConcatenationSugar() {
		aName.set("world");
		
		// this is syntactic sugar in case of simple concatenations, see above
		bName <= "hello, " + aName
		assertEquals("hello, world", bName.get());
		
		aName.set("Mondo");
		assertEquals("hello, Mondo", bName.get());
	}
	
	/** Test to make sure lists of PropertyChangeListener work, not just one, just for non-regression of a bug I had initially */
	@Test def testTwoBindings() {
		val Property<String> cName = new PropertyImpl<String>();
		aName <= "hello"
		bName <= aName
		cName <= aName
		assertEquals("hello", aName.get());
		assertEquals("hello", bName.get());
		assertEquals("hello", cName.get());
		aName <= "world"
		assertEquals("world", cName.get());
		assertEquals("world", bName.get());
	}
	
	// TODO consider & test exception handling in assigner 

	// TODO now write wrappers for EMF and (AOP-enhanced?) Beans and JFace (all using EDB ?), and Vaadin!
		
	// TODO think about list bindings
	// TODO think about master detail type bindings
	
	// TODO detect infinite loops
	// TODO handle interdependencies
	
	// TODO later, try using xtend Processed Annotations thing for something like @Bean ?
	 
}