/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings.property

import ch.vorburger.xbindings.XBinding

/**
 * XBindings Xtend extension methods & operator overloading sugar.
 *  
 * @author Michael Vorburger
 */
class XBindings {
	
	def static XBinding bind(()=>void assigner) {
		new XBinding(assigner); 
	}
	
	def static void operator_lessEqualsThan(Property<String> x, Property<String> y) {
 		bind[| x.set(y.get)]
 	}

	def static void operator_spaceship(Property<String> x, Property<String> y) {
		bind[| x.set(y.get)]
		bind[| y.set(x.get)]
	}

	def static void operator_doubleArrow(Property<String> x, Property<String> y) {
 		bind[| y.set(x.get)]
 	}

	def static <T> void operator_lessEqualsThan(Property<T> x, T y) {
 		// NOT bind[| x.set(y)] but simply:
 		x.set(y)
 	}

	def static Property<String> operator_plus(String x, Property<String> y) {
		val p = new PropertyImpl<String>()
		bind[| p.set(x + y.get)]
		return p
	}	
	
	def static Property<String> operator_plus(Property<String> x, String y) {
		val p = new PropertyImpl<String>()
		bind[| p.set(x.get + y)]
		return p
	}
	
	def static Property<String> operator_plus(Property<String> x, Property<String> y) {
		val p = new PropertyImpl<String>()
		bind[| p.set(x.get + y.get)]
		return p
	}
}