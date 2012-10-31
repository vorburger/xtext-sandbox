
package org.xtext.example.usages;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class UsagesStandaloneSetup extends UsagesStandaloneSetupGenerated{

	public static void doSetup() {
		new UsagesStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

