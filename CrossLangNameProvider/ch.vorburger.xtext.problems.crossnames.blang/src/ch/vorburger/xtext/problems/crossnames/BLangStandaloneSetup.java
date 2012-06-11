
package ch.vorburger.xtext.problems.crossnames;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class BLangStandaloneSetup extends BLangStandaloneSetupGenerated{

	public static void doSetup() {
		new BLangStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

