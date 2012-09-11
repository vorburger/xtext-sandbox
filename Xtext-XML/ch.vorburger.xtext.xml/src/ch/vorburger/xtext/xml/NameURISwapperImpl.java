/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;

import com.google.inject.Inject;

/**
 * Default Implementation of NameURISwapper.
 *
 * @author Michael Vorburger
 */
public class NameURISwapperImpl implements NameURISwapper {
	
    // intentionally protected - there should be no need for this prefix to be visible outside.  You might want to use the isNameScheme() helper  
    private static final String NAME_SCHEME = "name";

    private @Inject IQualifiedNameProvider nameProvider;
    private @Inject IQualifiedNameConverter nameConverter;
	
    private @Inject IScopeProvider scopeProvider;
	
	@Override
	public <T extends EObject> T cloneAndReplaceAllReferencesByNameURIProxies(T rootObject) {
		// Note that EcoreUtil2.clone / ECoreUtil.copy (should) already resolve all proxies in the original
		T clonedRootObject = EcoreUtil.copy(rootObject);
		replaceReferences(clonedRootObject, new XTextByNameURIReferenceReplacer());
		return clonedRootObject;
	}

    @Override
	public void replaceAllNameURIProxiesByReferences(EObject rootObject) {
        replaceReferences(rootObject, new NameURIByXTextReferenceReplacer());
    }

    protected <T extends EObject> void replaceReferences(T rootObject, ReferenceReplacer r) {
        Map<EObject, Collection<Setting>> map = EcoreUtil.CrossReferencer.find(Collections.singleton(rootObject));
        for (Map.Entry<EObject, Collection<EStructuralFeature.Setting>> entry : map.entrySet()) {
            EObject crossReference = entry.getKey();
            for (Setting setting : entry.getValue()) {
                EReference eReference = (EReference)setting.getEStructuralFeature();
                
                if (eReference.isContainment())
                    continue;
                if (!eReference.isChangeable())
                    continue;

//                if (!crossReference.eIsProxy())
//                    continue;
//                if (eReference.isContainment())
//                    throw new IllegalArgumentException(NameURISwapper.class + " doesn't know how to deal with a Proxy EReference which isContained: " + crossReference.toString());
//                if (!eReference.isChangeable())
//                    throw new IllegalArgumentException(NameURISwapper.class + " doesn't know how to deal with a Proxy EReference which isNotChangeable: " + crossReference.toString());
                
                final EObject object = setting.getEObject();
                if (!eReference.isMany()) {
                    EObject newProxyOrProxy = r.replaceReference(object, eReference, crossReference);
                    setting.set(newProxyOrProxy);
                } else {
                    @SuppressWarnings("unchecked")
                    List<EObject> list = (List<EObject>) setting.get(false); // == object.eGet(eReference);
                    for (int i = 0; i < list.size(); i++) {
                        EObject crossReferenceInList = list.get(i);
                        EObject newProxyOrProxy = r.replaceReference(object, eReference, crossReferenceInList);
                        list.set(i, newProxyOrProxy);
                    }
                }
            }
        }
    }
    
    protected interface ReferenceReplacer {
	    EObject replaceReference(EObject eObject, EReference eReference, EObject crossReference);
	}
	
    protected class XTextByNameURIReferenceReplacer implements ReferenceReplacer {
        @Override
        public EObject replaceReference(EObject eObject, EReference eReference, EObject crossReference) {
            //System.out.println("Going to replace: " + eReference + "\n  in: " + eObject + "\n  from currently " + crossReference);
            if (crossReference.eIsProxy()) {
                URI proxyURI = ((InternalEObject)crossReference).eProxyURI();
                if (NAME_SCHEME.equals(proxyURI.scheme()))
                    // Sometimes CrossReferencer gives us duplicates, which we already processed..
                    return crossReference;
                        
                String nodeText = "";
                List<INode> nodes = NodeModelUtils.findNodesForFeature(eObject, eReference);
                if (nodes.size() > 0)
                    nodeText = NodeModelUtils.getTokenText(nodes.get(0));
                throw new IllegalArgumentException(NameURISwapperImpl.class + " found a Proxy when an non-Proxy EObject was expected - why did it not get resolved before reaching this point? URI=" + crossReference.toString() + ", eReference=" + eReference + ", NodeText=" + nodeText);
            }
            
//            final IScope scope = scopeProvider.getScope(setting.getEObject(), eReference);
//            final IEObjectDescription objectDescription = scope.getSingleElement(crossReference);
//            final QualifiedName qName = objectDescription.getQualifiedName();
            final QualifiedName qName = nameProvider.getFullyQualifiedName(crossReference);

            if (qName == null)
                throw new IllegalArgumentException(NameURISwapperImpl.class + " IQualifiedNameProvider returned null for " + crossReference.toString());
            final String name = nameConverter.toString(qName);
            if (name == null)
                throw new IllegalArgumentException(NameURISwapperImpl.class + " IQualifiedNameConverter returned null for " + qName);
            
            // Note the '#' suffix... this is needed because otherwise the
            // org.eclipse.emf.ecore.xmi.impl.XMLHandler's setValueFromId()
            // does not create Proxy objects but just ignores such href from XML.
            final URI uri = URI.createURI(NAME_SCHEME + ":/" + name + "#"); 

            final EObject newProxy = EcoreUtil3.createProxy(uri, eReference.getEReferenceType());
    		((InternalEObject) newProxy).eSetProxyURI(uri);

            return newProxy;
       }
	    
	}
	
    protected class NameURIByXTextReferenceReplacer implements ReferenceReplacer {
        @Override
        public EObject replaceReference(EObject eObject, EReference eReference, EObject crossReference) {
            final URI uri = EcoreUtil.getURI(crossReference);
            if (NAME_SCHEME.equals(uri.scheme())) {
                // path() ignores the '#' which we added above
                final String crossRefString = uri.path().substring(1);
                final QualifiedName name = nameConverter.toQualifiedName(crossRefString);
                
                final IScope scope = scopeProvider.getScope(eObject, eReference);
                final IEObjectDescription objectDescription = scope.getSingleElement(name);
                if (objectDescription == null)
                	throw new IllegalArgumentException("IScope cannot resolve QualifiedName '" + crossRefString + "'");
                
                final EObject newObjectOrProxy = objectDescription.getEObjectOrProxy();
                return newObjectOrProxy;
            } else {
                return crossReference;
            }
        }
    }
    
    static public boolean isNameScheme(URI proxyURI) {
        if (proxyURI != null)
            return NAME_SCHEME.equals(proxyURI.scheme());
        else
            return false;
    }
}
