package ch.vorburger.databinding.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.internal.databinding.property.value.SimplePropertyObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

import ch.vorburger.databinding.tests.utils.DatabindingTestRealm;
import ch.vorburger.xtext.databinding.tests.utils.ECoreHelper;

@SuppressWarnings("restriction")
public class TestValuePropertyTest {
	
	// TODO LOW Look into Core DataBinding sources for stuff like this...
	
	// TODO LOW Actually, make this independent of EMF Data Binding - just test binding two TestValueProperties
	
	@Test
	public void test1() {
		// Create an ECore model
		ECoreHelper helper = new ECoreHelper();
		EDataType stringType = EcorePackage.eINSTANCE.getEString();
		EPackage pkg = helper.createPackage("tests");
		EClass clazz = helper.createClass(pkg, "Test");
		EAttribute titleFeature = helper.addAttribute(clazz, stringType, "title");

		// Create an EObject
		EObject eObject = helper.createInstance(clazz);
		eObject.eSet(titleFeature, "This is the Title");
		
		// Create a Property
		final TestValueProperty testValueProperty = new TestValueProperty();
		String title = "Initial";
		
		Realm realm = new DatabindingTestRealm();
		DataBindingContext db = new EMFDataBindingContext(realm);
		
		db.bindValue(
				new SimplePropertyObservableValue(realm, title, testValueProperty),
				EMFProperties.value(titleFeature).observe(eObject));
		
		assertEquals(eObject.eGet(titleFeature), testValueProperty.getValue(title));
		
		testValueProperty.setValue(title, "reset, reset");
		assertEquals("reset, reset", testValueProperty.getValue(title));
		assertEquals("reset, reset", eObject.eGet(titleFeature));

		db.dispose();
	}

}
