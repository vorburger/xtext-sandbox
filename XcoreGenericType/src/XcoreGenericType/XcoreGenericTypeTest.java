package XcoreGenericType;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.xtext.resource.XtextResourceSet;
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
	public void testXcoreGenericType() throws Exception {
		final String model = "src/XcoreGenericType/TestModel2.xcore";
		URI uri = URI.createFileURI(new File(model).getAbsolutePath());
		Resource resource = rs.getResource(uri, true);
		final EList<EObject> contents = resource.getContents();
		
		if (contents.isEmpty())
			throw new IOException("Could no load / no content in resource: " + model);
		
		BasicDiagnostic chain = new BasicDiagnostic();
		for (EObject content : contents) {
			Diagnostician.INSTANCE.validate(content, chain);
		}
		logResourceDiagnostics(resource);
		if (!BasicDiagnostic.toIStatus(chain).isOK()) {
			throw new DiagnosticExceptionWithURIAndToString(chain, uri);
		}	
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
