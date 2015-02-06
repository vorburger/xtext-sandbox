package XcoreGenericType;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.xcore.XcoreStandaloneSetup;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Case to illustrate a suspected Xcore bug?
 * 
 * Why does this fail with Diagnostic ERROR source=null code=0 null [Diagnostic ERROR source=org.eclipse.emf.ecore.model code=24 A generic type in this context must refer to a classifier or a type parameter data=[org.eclipse.emf.ecore.impl.EGenericTypeImpl@9600a4d (expression: ?), org.eclipse.emf.ecore.impl.EReferenceImpl@58a40859 (name: eClassifier) (ordered: true, unique: true, lowerBound: 0, upperBound: 1) (changeable: true, volatile: false, transient: false, defaultValueLiteral: null, unsettable: false, derived: false) (containment: false, resolveProxies: true), org.eclipse.emf.ecore.impl.EReferenceImpl@79203885 (name: eTypeParameter) (ordered: true, unique: true, lowerBound: 0, upperBound: 1) (changeable: true, volatile: false, transient: false, defaultValueLiteral: null, unsettable: false, derived: false) (containment: false, resolveProxies: false)]
 * 
 * @author Michael Vorburger
 */
// @RunWith(XtextRunner.class)
// @InjectWith(XcoreInjectorProvider.class)
public class XcoreGenericTypeTest {

	// @Inject 
	ResourceSet rs = new XtextResourceSet();
	
	@Test
	public void testXcoreGenericTypeWithValidation() throws Exception {
		XcoreStandaloneSetup.doSetup();
		loadModel(URI.createURI("classpath:/model/Ecore.ecore"), true);
		loadModel(URI.createURI("classpath:/model/Ecore.genmodel"), true);
		loadModelFromFile("src/XcoreGenericType/TestModel2.xcore", true);	
	}

	@Test
	public void testXcoreGenericTypeWithoutValidation() throws Exception {
		XcoreStandaloneSetup.doSetup();
		loadModel(URI.createURI("classpath:/model/Ecore.ecore"), true);
		loadModel(URI.createURI("classpath:/model/Ecore.genmodel"), true);
		// index 0 has xtext AST model; index 1 has what we want
		GenModel genModel = (GenModel) loadModelFromFile("src/XcoreGenericType/TestModel2.xcore", false).get(1);
		final GenPackage genPackage = genModel.getGenPackages().get(0);
		Assert.assertEquals("testmodel2", genPackage.getNSName());
		final GenClass genClass = genPackage.getGenClasses().get(0);
		GenFeature genFeature = genClass.getGenFeatures().get(0);
		EStructuralFeature feature = genFeature.getEcoreFeature();
		EAttribute attribute = (EAttribute) feature;
		Assert.assertEquals("age", attribute.getName());
		Assert.assertEquals("EInt", attribute.getEType().getName());
	}

	private EList<EObject> loadModelFromFile(final String modelFileName, boolean validate) throws IOException, DiagnosticExceptionWithURIAndToString {
		URI uri = URI.createFileURI(new File(modelFileName).getAbsolutePath());
		return loadModel(uri, validate);
	}
	
	private EList<EObject> loadModel(final URI uri, boolean validate) throws IOException, DiagnosticExceptionWithURIAndToString {
		Resource resource = rs.getResource(uri, true);
		final EList<EObject> contents = resource.getContents();
		
		if (contents.isEmpty())
			throw new IOException("Could no load / no content in resource: " + uri.toString());
		
		if (validate) {
			BasicDiagnostic chain = new BasicDiagnostic();
			for (EObject content : contents) {
				Diagnostician.INSTANCE.validate(content, chain);
			}
			logResourceDiagnostics(resource);
			if (!BasicDiagnostic.toIStatus(chain).isOK()) {
				throw new DiagnosticExceptionWithURIAndToString(chain, uri);
			}
		}
		
		return contents;
	}

	private void logResourceDiagnostics(Resource resource) {
		for (Diagnostic diag : resource.getErrors()) {
			System.err.println("ERR in test resource: " + resource.getURI() + " :: " + diag.getMessage());
		}
		for (Diagnostic diag : resource.getWarnings()) {
			System.out.println("WARN in test resource: " + resource.getURI() + " :: " + diag.getMessage());
		}
	}
}
