/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.xml;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;

/**
 * Few more Ecore utilities.
 * 
 * @see EcoreUtil2
 * @see EcoreUtil
 * 
 * @author Michael Vorburger
 */
public class EcoreUtil3 extends EcoreUtil2 {

	public static <T extends EObject> T cloneWithProxiesIfContained(T eObject) {
		if (eObject.eContainer()!=null || eObject.eResource()!=null)
			return cloneWithProxies(eObject);
		return eObject;
	}

	public static EObject createProxy(URI uri, EClass eClass) {
		// Strange that this doesn't exist elsewhere... or can I just not find it?
		EObject proxy = EcoreUtil.create(eClass);
		((InternalEObject) proxy).eSetProxyURI(uri);
		return proxy;
	}
}
