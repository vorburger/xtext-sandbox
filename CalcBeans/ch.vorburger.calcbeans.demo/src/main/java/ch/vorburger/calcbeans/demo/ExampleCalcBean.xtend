package ch.vorburger.calcbeans.demo

import org.eclipse.xtend.lib.annotations.Accessors
import ch.vorburger.calcbeans.annotations.Calc

@Calc
@Accessors  
class ExampleCalcBean {

	Integer a
	
	Integer b
	
	Integer c = a * b
	
}