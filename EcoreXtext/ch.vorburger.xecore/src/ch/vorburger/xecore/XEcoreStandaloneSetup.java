/*
* generated by Xtext
*/
package ch.vorburger.xecore;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class XEcoreStandaloneSetup extends XEcoreStandaloneSetupGenerated{

	public static void doSetup() {
		new XEcoreStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
