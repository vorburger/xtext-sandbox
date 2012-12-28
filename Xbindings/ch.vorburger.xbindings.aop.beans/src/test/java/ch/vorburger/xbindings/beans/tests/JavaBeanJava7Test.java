/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings.beans.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.junit.Test;

import ch.vorburger.xbindings.XBinding;

/**
 * XBindings JavaBean Test (and example) written in Java 7 instead of Xtend (without lambda expressions / closures).
 * 
 * @author Michael Vorburger
 */
public class JavaBeanJava7Test {

	// TODO try to write this with Java v8 lambda expressions (closures) !
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=380188

	@Test
	public void testBasic() {
		final TestJavaBean bean = new TestJavaBean();
		bean.setValue(42);
		
		new XBinding(new Procedure0() {
			public void apply() {
				bean.setName("hello, " + bean.getValue());
				
			}
		});
		
		assertEquals("hello, 42", bean.getName());
		
		bean.setValue(1);
		assertEquals("hello, 1", bean.getName());
	}
}
