
package org.xtext.example.definitions;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class DefinitionsStandaloneSetup extends DefinitionsStandaloneSetupGenerated{

	public static void doSetup() {
		new DefinitionsStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

