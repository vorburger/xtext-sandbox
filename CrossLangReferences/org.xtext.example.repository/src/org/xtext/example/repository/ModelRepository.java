package org.xtext.example.repository;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.SynchronizedXtextResourceSet;

/**
 * Repository of Models.
 * 
 * @author Michael Vorburger
 */
public class ModelRepository {

	// Just for demo - in real life, this is associated with an IProject in a Map somewhere
	//private static ResourceSet rs = new SynchronizedXtextResourceSet();

	public static EObject getModel(IProject project, URI uri) {
		ResourceSet rs = getResourceSet(project);
		Resource resource = rs.getResource(uri, true);
		EObject model = resource.getContents().get(0);
		return model;
	}

	private static ResourceSet getResourceSet(IProject project) {
		return new SynchronizedXtextResourceSet();
	}

}
