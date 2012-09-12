/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.xml;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.tokens.CrossReferenceSerializer;

/**
 * CrossReferenceSerializer with support for {@link NameURISwapper}.
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class NameURISupportingCrossReferenceSerializer extends CrossReferenceSerializer {

	@Override
	public String serializeCrossRef(EObject semanticObject, CrossReference crossref, EObject target, INode node, Acceptor errors) {
		String namedRef = NameURISwapperImpl.getNameFromProxy(target);
		if (namedRef != null) {
			return namedRef;
		} else {
			return super.serializeCrossRef(semanticObject, crossref, target, node, errors);
		}
	}

}
