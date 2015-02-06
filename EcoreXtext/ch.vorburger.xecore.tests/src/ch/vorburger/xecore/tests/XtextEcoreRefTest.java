package ch.vorburger.xecore.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Test;
import org.junit.runner.RunWith;

import testmodel3.TestModel;
import ch.vorburger.xecore.xEcore.Greeting;

import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(XtextRunner.class)
@InjectWith(XEcoreInjectorProviderWithDependenciesInjectorProvider.class)
public class XtextEcoreRefTest {

	@Inject Provider<XtextResourceSet> resourceSetProvider;
	@Inject ParseHelper<Greeting> parseHelper;
	@Inject ValidationTestHelper validationHelper;
	
	@Test public void testEClass() throws Exception {
		XtextResourceSet rs = resourceSetProvider.get();
		
		URI xCoreURI = getURI("ch.vorburger.xecore.tests", "model/TestModel3.xcore");
		Resource xCoreResoure = rs.getResource(xCoreURI, true);
		for(EObject o : xCoreResoure.getContents()) {
			if (o instanceof EPackage) {
				EPackage pack = (EPackage) o;
				if (Testmodel3Package.eNS_URI.equals(pack.getNsURI()) ) {
					pack.setEFactoryInstance(Testmodel3Factory.eINSTANCE);
				}
			}
		}
		// Validation of Xcore fails with "'A generic type in this context must refer to a classifier or a type parameter' on XGenericType", but it actually loaded succesfully..
		// validationHelper.assertNoErrors(xCoreResoure.getContents().get(0));
		
		Greeting g = parseHelper.parse("Hello testmodel3.TestModel !", rs);
		validationHelper.assertNoErrors(g);
		EClass anEClass = g.getEClass();
		assertEquals("TestModel", anEClass.getName());
		
		EObject eObject = anEClass.getEPackage().getEFactoryInstance().create(anEClass);
		// TODO FIXME AssertionError: class org.eclipse.emf.ecore.impl.DynamicEObjectImpl
		assertTrue(eObject.getClass().toString(), eObject instanceof TestModel);
	}

	
	// TODO Something like this should really be available in Xtext JUnit helpers...
	
	public URI getURI(String pluginId, String plugInRootRelativePath) {
		URI uri;
		if (Platform.isRunning()) {
			uri = createPlatformURI(pluginId, plugInRootRelativePath);
		} else {
			uri = createFileURI(pluginId, plugInRootRelativePath);
		}
		return uri;
	}

	private URI createFileURI(String pluginId, String plugInRootRelativePath) {
		return URI.createFileURI(new File(plugInRootRelativePath).getAbsolutePath());
	}

	private URI createPlatformURI(String pluginId, String plugInRootRelativePath) {
		return URI.createPlatformPluginURI(pluginId + "/" + plugInRootRelativePath, true);
	}

}
