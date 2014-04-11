package org.xtext.example.mydsl

import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.resource.IResourceDescriptions

class SpecialResourceDescriptionsProvider extends ResourceDescriptionsProvider {
	
	public static val INDEX = "index"
	
	override getResourceDescriptions(ResourceSet resourceSet) {
		val index = resourceSet.loadOptions.get(INDEX)
		if (index instanceof IResourceDescriptions) {
			return index
		}
		super.getResourceDescriptions(resourceSet)
	}
	
}