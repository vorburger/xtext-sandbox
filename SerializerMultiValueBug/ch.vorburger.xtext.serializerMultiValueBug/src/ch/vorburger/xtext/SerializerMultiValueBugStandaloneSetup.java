
package ch.vorburger.xtext;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class SerializerMultiValueBugStandaloneSetup extends SerializerMultiValueBugStandaloneSetupGenerated{

	public static void doSetup() {
		new SerializerMultiValueBugStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

