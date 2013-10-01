package ch.vorburger.xtext.XbaseOptionResolveAllConfusion.tests;

import ch.vorburger.xtext.XbaseOptionResolveAllConfusion.ELInjectorProvider
import javax.inject.Inject
import javax.inject.Provider
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.util.StringInputStream
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.eclipse.xtext.xbase.compiler.XbaseCompiler
import org.eclipse.xtext.xbase.compiler.output.FakeTreeAppendable
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Illustration of a confusing Xbase issue I ran into
 * while upgrading Vorburger/ELang from Xtext v2.3.1 to v2.4.3.
 * 
 * Note the ch.vorburger.xtext.XbaseOptionResolveAllConfusion.jvmmodel.ELJvmModelInferrer which is needed for this.
 * 
 * @see http://www.eclipse.org/forums/index.php/mv/msg/532865/
 * 
 * @author Michael Vorburger
 */
@RunWith(XtextRunner)
@InjectWith(ELInjectorProvider)
public class XbaseOptionResolveAllConfusionTest {

	@Inject Provider<XtextResourceSet> resourceSetProvider;
	@Inject extension ParseHelper<XExpression> parseHelper
	@Inject extension ValidationTestHelper
	@Inject JvmTypesBuilder jvmTypesBuilder;
	@Inject XbaseCompiler compiler;

	@Test
	def void testOK() {
		val xExpression = "\"Saluton\" == null".parse
		checkXExpression(xExpression)
	}

	@Test
	def void testStillOK() {
		val xExpression = parseHelper.parse(
			new StringInputStream("\"Saluton\" == null"), 
			URI.createURI("__synthetic1." + fileExtension), // make sure a different URI is used here than below
			null,
			resourceSetProvider.get())
		checkXExpression(xExpression)
	}

	@Test
	def void testExpectedThisToFailButStrangelyHereItWorks() {
		val resourceSet = resourceSetProvider.get();
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		// val options = newLinkedHashMap(XtextResource.OPTION_RESOLVE_ALL -> Boolean.TRUE)
		val xExpression = parseHelper.parse(
			new StringInputStream("\"Saluton\" == null"),
			URI.createURI("__synthetic2." + fileExtension), // make sure a different URI is used here than above
			resourceSet.getLoadOptions(),
			resourceSet)
		checkXExpression(xExpression)
	}
	
	def checkXExpression(XExpression xExpression) {
		xExpression.assertNoErrors
		
		val expectedJvmType = jvmTypesBuilder.newTypeRef(xExpression, Number)		
		
		// ch.vorburger.el.engine.ExpressionImpl.generateJavaCode(Class<?>)
		val ImportManager importManager = new ImportManager(false);
		val FakeTreeAppendable appendable = new FakeTreeAppendable(importManager);
		val actualJava = compiler.compileAsJavaExpression(xExpression, appendable, expectedJvmType).toString();
		
		Assert::assertEquals('''
			new org.eclipse.xtext.xbase.lib.Functions.Function0<Number>() {
			  public Number apply() {
			    boolean _equals = com.google.common.base.Objects.equal("Saluton", null);
			    return _equals;
			  }
			}.apply()'''.toString, actualJava)
	}

}
