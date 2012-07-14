
package org.eclipse.xtext.example.fowlerdsl;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl;

import com.google.inject.Injector;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class StatemachineStandaloneSetup extends StatemachineStandaloneSetupGenerated{

	public static void doSetup() {
		new StatemachineStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	@Override
	public Injector createInjectorAndDoEMFRegistration() {
        // Because we do test reading & writing XML, this is needed as well:
        if (!Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().containsKey("xml"))
        	Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new GenericXMLResourceFactoryImpl());
		return super.createInjectorAndDoEMFRegistration();
	}
	
	
}

