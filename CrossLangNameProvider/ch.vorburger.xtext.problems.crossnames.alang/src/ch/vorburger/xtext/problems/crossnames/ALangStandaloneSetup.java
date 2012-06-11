
package ch.vorburger.xtext.problems.crossnames;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ALangStandaloneSetup extends ALangStandaloneSetupGenerated{

	public static void doSetup() {
		new ALangStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

