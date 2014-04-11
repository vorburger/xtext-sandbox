package org.xtext.example.mydsl.generator

import javax.inject.Inject
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IFileSystemAccessExtension2
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.resource.SaveOptions
import org.xtext.example.mydsl.myDsl.Greeting
import org.xtext.example.mydsl.myDsl.MyDslFactory
import org.eclipse.xtext.resource.impl.ResourceSetBasedResourceDescriptions
import com.google.inject.Provider
import org.eclipse.xtext.resource.XtextResource
import org.xtext.example.mydsl.SpecialResourceDescriptionsProvider

class MyDslGenerator implements IGenerator {
	
	// This is JUST AN EXAMPLE, to illustrate a problem to post on Forum etc.
	// This, as is, DOES NOT REALLY MAKE SENSE...
	
	@Inject Provider<ResourceSet> rsProvider;
	
	@Inject Provider<ResourceSetBasedResourceDescriptions> descriptionsProvider
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		// ignore input resource.. ;) see above!
		val rs = freshResourceSet()
		val resourceB = rs.freshResource("b", fsa)
		val Greeting bG = MyDslFactory::eINSTANCE.createGreeting
		bG.name = "b";
		resourceB.contents += bG
		resourceB.save(null)
		
		val resourceA = rs.freshResource("a", fsa)
		val Greeting aG = MyDslFactory::eINSTANCE.createGreeting
		aG.name = "a";
		aG.anotherGreeting = bG;
		resourceA.contents += aG
		resourceA.save(null)
	}
	
	def freshResourceSet() {
		val rs = rsProvider.get
		val descriptions = descriptionsProvider.get
		descriptions.setContext(rs)
		rs.loadOptions.put(SpecialResourceDescriptionsProvider.INDEX, descriptions)
		return rs
	}
	
	def Resource freshResource(ResourceSet rs, String name, IFileSystemAccess fsa) {
		val IFileSystemAccessExtension2 fsa2 = fsa as IFileSystemAccessExtension2
		val URI uri = fsa2.getURI(name + ".mydsl")
		return rs.createResource(uri)
	}
	
//	def void serialize(Resource resource, IFileSystemAccess fsa) {
//		val SaveOptions options = SaveOptions.newBuilder().noValidation().getOptions();
//		resource.save(options.toOptionsMap());		
//	}
}
