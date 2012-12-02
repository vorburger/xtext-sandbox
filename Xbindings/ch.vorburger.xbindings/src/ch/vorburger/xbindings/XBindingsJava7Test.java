/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * XBindings Test (and example) written in Java (without lambda expressions / closures).
 * 
 * @author Michael Vorburger
 */
public class XBindingsJava7Test {

	// TODO try to write this with Java v8 lambda expressions (closures) !
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=380188

	Property<String> aName = new PropertyImpl<String>();
	Property<String> bName = new PropertyImpl<String>();
	
	@Test
	public void testBasic() {
		aName.set("Yuhu");
		
		new XBinding() {
			void bind() {
				bName.set("hello, " + aName.get());
			}
		};
		
		assertEquals("hello, Yuhu", bName.get());
		
		aName.set("Yoho");
		assertEquals("hello, Yoho", bName.get());
	}
}
