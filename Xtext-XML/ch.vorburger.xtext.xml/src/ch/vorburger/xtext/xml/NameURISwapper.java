/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.xml;

import org.eclipse.emf.ecore.EObject;

/**
 * Util which deals with name:/Something URI Proxies.
 * This is used to transform EMF XML containing name-based cross references into real Xtext objects, and vice versa.  
 *
 * @author Michael Vorburger
 */
public interface NameURISwapper {

	/**
	 * Replaces xText's internal xtextLink_::0::3::/15 Proxy URIs with name:/Something URI Proxies.
	 * This is used before writing XML.
	 */
	<T extends EObject> T cloneAndReplaceAllReferencesByNameURIProxies(T rootObject);

	/**
	 * Replaces our name:/Something URI Proxies by valid xText's referenced objects or internal xtextLink_::0::3::/15-style proxies.
	 * This is used after having read XML containing name:/Something URI Proxies.
	 */
	void replaceAllNameURIProxiesByReferences(EObject rootObject);

}