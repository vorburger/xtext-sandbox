package ch.vorburger.xecore.tests;

import org.eclipse.emf.ecore.xcore.XcoreStandaloneSetup;
import org.eclipse.xtext.ecore.EcoreSupportStandaloneSetup;

import testmodel3.impl.Testmodel3PackageImpl;

import com.google.inject.Injector;

import ch.vorburger.xecore.XEcoreInjectorProvider;

public class XEcoreInjectorProviderWithDependenciesInjectorProvider extends XEcoreInjectorProvider {

	@Override
	protected Injector internalCreateInjector() {
		Testmodel3PackageImpl.init();
		EcoreSupportStandaloneSetup.setup();
		XcoreStandaloneSetup.doSetup();
		return super.internalCreateInjector();
	}

}
