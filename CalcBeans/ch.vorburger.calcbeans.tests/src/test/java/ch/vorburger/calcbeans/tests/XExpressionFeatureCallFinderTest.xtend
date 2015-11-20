package ch.vorburger.calcbeans.tests

import ch.vorburger.calcbeans.annotations.internal.XExpressionFeatureCallFinder
import ch.vorburger.calcbeans.tests.copypasted.XbaseInjectorProvider
import com.google.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.xbase.XExpression
import org.junit.Test
import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.validation.ValidationTestHelper

@RunWith(XtextRunner)
@InjectWith(XbaseInjectorProvider)
class XExpressionFeatureCallFinderTest {
	
	@Inject XExpressionFeatureCallFinder f
	@Inject ParseHelper<XExpression> parser
	@Inject ValidationTestHelper valid
	
	@Test def void test1() {
		val e = parser.parse("class A { String a } a")
		valid.assertNoErrors(e)
		e.eAllContents.forEach[System.out.println(it.toString)]
		//val list = f.findFeatureCalls(e)
	}
	
}