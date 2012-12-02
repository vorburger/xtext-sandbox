/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings

/**
 * XBindings Xtend extension methods & operator overloading.
 *  
 * @author Michael Vorburger
 */
class XBindings {
	
	def static bind(()=>void assigner) { 
		PropertyAccessTrackerUtil::ThreadLocal.set( [| assigner.apply() ] )
		try {
			assigner.apply // initial assignment
		} finally {
			PropertyAccessTrackerUtil::ThreadLocal.remove();
		}
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