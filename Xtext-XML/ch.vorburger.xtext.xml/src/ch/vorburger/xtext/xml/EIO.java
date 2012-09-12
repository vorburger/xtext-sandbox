/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.xml;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.SaveOptions;

import com.google.inject.Inject;

/**
 * EMF I/O Utils.
 * Helpers for loading & saving EMF models as XML & DSL.
 * 
 * The XML loading doesn't do much useful validation - the idea is that
 * something else will validate the EMF model afterwards - such as an Xtext
 * Resource serialization and validation rules. You would typically use the
 * Export Model > XML Schema (non XMI) on the .genmodel to obtain a suitable XSD
 * to illustrate how your XML has to look like for this.
 * 
 * @author Michael Vorburger
 */
public class EIO {

    protected @Inject ResourceSet resourceSet;
    
    /**
     * Saves EMF object, as XML or DSL, depending on the extension of the URI.
     * The object is cloned before it is saved, because the purpose of this
     * utility is to allow conversion between XML <-> DSL, without changing the
     * original. If you don't need that, you would simply use
     * <code>object.eResource().save(null)</code> instead.
     */
    public void cloneAndSave(URI uri, EObject object) throws IOException {
        Resource resource = resourceSet.createResource(uri);
        if (resource == null)
            throw new IOException("EMF resourceSet.getResource() => null, probably no matching resource factory is registered, for URI: " + uri.toString());
        EObject clonedObject = object; // TODO give up? EcoreUtil3.cloneWithProxiesIfContained(object);
        resource.getContents().add(clonedObject);
        // Don't Validate, but Format (which is the exact opposite of the default save options) :
        Map<?, ?> saveOptions = SaveOptions.newBuilder().noValidation().format().getOptions().toOptionsMap();
        resource.save(saveOptions);
        // Because our special NameProvider do some Magic (name:/ Proxy URI handling), it is probably safest if we unload the resource so it gets cleanly reloaded on next access?
        resource.unload();
    }

    public Resource loadResource(URI uri) throws IOException, DiagnosticException {
        // NOTE: For the XtextResourceSet classpath resolving URIs, must use getResource() instead of a createResource() followed by a resource.load(resource.getResourceSet().getLoadOptions()); 
        Resource resource = resourceSet.getResource(uri, true);
        if (resource == null)
            throw new IOException("EMF resourceSet.getResource() => null, probably no matching resource factory is registered, for URI: " + uri.toString());
        
        // TODO Move this to a separate helper method, don't force on load..
        if (!resource.getErrors().isEmpty()) {
            Diagnostic diagnostic = EcoreUtil.computeDiagnostic(resource, false /* do not includeWarnings */);
            throw new DiagnosticExceptionWithURIAndToString(diagnostic, uri);
        }
        
        return resource;
    }

    @SuppressWarnings("unchecked")
    public <T extends EObject> T load(URI uri, Class<T> klazz) throws IOException, DiagnosticException {
        return (T) load(uri);
    }

    public EObject load(URI uri) throws IOException, DiagnosticException {
        Resource resource = loadResource(uri);
        if (resource != null && !resource.getContents().isEmpty())
            return resource.getContents().get(0);
        else
            throw new IllegalArgumentException("Loading failed, resource == null, or no contents: " + uri.toString());
    }
}
