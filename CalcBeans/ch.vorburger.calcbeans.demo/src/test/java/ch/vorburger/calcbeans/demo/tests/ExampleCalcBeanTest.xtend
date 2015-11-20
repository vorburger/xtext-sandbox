package ch.vorburger.calcbeans.demo.tests

import org.junit.Test

import static org.junit.Assert.*
import ch.vorburger.calcbeans.demo.ExampleCalcBean

class ExampleCalcBeanTest {
	
	@Test def testAB() {
		val calc = new ExampleCalcBean
		calc.a = 3
		calc.b = 7
		assertEquals(21, calc.c)
		
		calc.b = 5
		assertEquals(15, calc.c)
		
		calc.c = 19 // override...
		assertEquals(19, calc.c)
	}
}