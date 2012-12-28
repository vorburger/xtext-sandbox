/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings.beans.tests

import org.junit.Test

import static org.junit.Assert.*
import static extension ch.vorburger.xbindings.property.XBindings.*

/**
 * XBindings JavaBean Test (and example) written in Xtend.
 * 
 * @see http://www.eclipse.org/xtend/
 * @author Michael Vorburger
 */
class JavaBeanTest {
	
	@Test def testOneWayBinding() {
		val bean = new TestJavaBean
		bean.value = 42
		bind([| bean.name = '''hello, «bean.value»''' ])
		assertEquals("hello, 42", bean.name);
		bean.value = 1;
		assertEquals("hello, 1", bean.name);
	}
	 
}