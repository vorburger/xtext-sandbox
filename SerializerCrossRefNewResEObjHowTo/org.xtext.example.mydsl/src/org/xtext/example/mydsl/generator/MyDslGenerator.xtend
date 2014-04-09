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

class MyDslGenerator implements IGenerator {
	
	// This is JUST AN EXAMPLE, to illustrate a problem to post on Forum etc.
	// This, as is, DOES NOT REALLY MAKE SENSE...
	
	@Inject ResourceSet rs;
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		// ignore input resource.. ;) see above!
		
		val Greeting bG = MyDslFactory::eINSTANCE.createGreeting
		bG.name = "b";
		bG.serialize(fsa);
		
		val Greeting aG = MyDslFactory::eINSTANCE.createGreeting
		aG.name = "a";
		aG.anotherGreeting = bG;
		aG.serialize(fsa);
	}
	
	def void serialize(Greeting g, IFileSystemAccess fsa) {
		val IFileSystemAccessExtension2 fsa2 = fsa as IFileSystemAccessExtension2
		val URI uri = fsa2.getURI(g.name + ".mydsl")
		val Resource resource = rs.createResource(uri)
		resource.contents.add(g)
		val SaveOptions options = SaveOptions.newBuilder().noValidation().getOptions();
		resource.save(options.toOptionsMap());		
	}
}
