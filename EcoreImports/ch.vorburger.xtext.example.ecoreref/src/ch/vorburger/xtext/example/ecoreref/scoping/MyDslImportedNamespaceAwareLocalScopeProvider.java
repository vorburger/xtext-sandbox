/*******************************************************************************
 * Copyright (c) 2013 Michael Vorburger and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   Michael Vorburger - Initial API and implementation
 ******************************************************************************/
package ch.vorburger.xtext.example.ecoreref.scoping;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.eclipse.xtext.util.Strings;

import ch.vorburger.xtext.example.ecoreref.myDsl.Import;
import ch.vorburger.xtext.example.ecoreref.myDsl.Model;
import ch.vorburger.xtext.example.ecoreref.myDsl.MyDslPackage;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * ImportedNamespaceAwareLocalScopeProvider which handles some things differently.
 * 
 * @author Michael Vorburger
 */
public class MyDslImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

	@Inject
	// Has to be repeated because it's private in ImportedNamespaceAwareLocalScopeProvider and no getQualifiedNameConverter() :(
	// @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=409003
	private IQualifiedNameConverter nameConverter;

	/**
	 * We override this because in our grammar, the importedNamespace is of type EPackage instead of a String.
	 */
	@Override
	protected List<ImportNormalizer> internalGetImportedNamespaceResolvers(EObject context, boolean ignoreCase) {
	    if (!(context instanceof Model))
	      return Collections.emptyList();
	    Model model = (Model)context;
	    List<ImportNormalizer> importedNamespaceResolvers = Lists.newArrayList();
	    
	    for (Import anImport : model.getImports()) {
	    	// We CANNOT do this here: 
	    	//    EPackage ePackage = anImport.getImportedEPackage();
	        //    QualifiedName packageQN = getQualifiedNameProvider().getFullyQualifiedName(ePackage);
	        //    String packageName = nameConverter.toString(packageQN);
	    	// because that would lead to a ""Cyclic resolution of lazy links".. so, instead:
	    	List<INode> nodes = NodeModelUtils.findNodesForFeature(anImport, MyDslPackage.Literals.IMPORT__IMPORTED_EPACKAGE);
	        INode node = nodes.get(0);
	        final String packageName = NodeModelUtils.getTokenText(node);
			importedNamespaceResolvers.add(createImportedNamespaceResolver(packageName, ignoreCase));
		}
	    return importedNamespaceResolvers;
	}
	  
	/**
	 * We override this because in our grammar, the trailing .* isn't part of the rule, so that we can use a reference.  
	 */
	@Override
	protected ImportNormalizer createImportedNamespaceResolver(final String namespace, boolean ignoreCase) {
		if (Strings.isEmpty(namespace))
			return null;
		QualifiedName importedNamespace = nameConverter.toQualifiedName(namespace);
		if (importedNamespace == null || importedNamespace.getSegmentCount() < 1) {
			return null;
		}
		// We know our language has a wildcard, but it's in the Grammar (not in Rule)
		// instead of being part of the namespace String, so this can be simplified, 
		// and should not use skipLast(1)
		return doCreateImportNormalizer(importedNamespace, true, ignoreCase);
	}
}
