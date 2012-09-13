package ch.vorburger.xtext.databinding.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

import ch.vorburger.beans.AbstractPropertyChangeNotifier;
import ch.vorburger.xtext.databinding.XtextDataBindingContext;
import ch.vorburger.xtext.databinding.tests.utils.DatabindingTestRealm;
import ch.vorburger.xtext.databinding.tests.utils.ECoreHelper;

public class EMFXtextPropertiesTest {

	@SuppressWarnings("serial")
	private static class Bean extends AbstractPropertyChangeNotifier {
		private String name;
		public void setName(String name) {
			firePropertyChange("name", this.name, this.name = name);
		}
		public String getName() {
			return name;
		}
	}
	
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
		
		// Create a Bean
		Bean bean = new Bean();
		
		Realm realm = new DatabindingTestRealm();
		DataBindingContext db = new XtextDataBindingContext(realm);
		
		db.bindValue(BeanProperties.value("name").observe(bean),
				// TODO Use EMFXtextProperties instead!
				EMFProperties.value(titleFeature).observe(eObject));
		
		assertEquals(eObject.eGet(titleFeature), bean.getName());
		
		bean.setName("reset, reset");
		assertEquals("reset, reset", bean.getName());
		assertEquals("reset, reset", eObject.eGet(titleFeature));

		db.dispose();
	}

}
