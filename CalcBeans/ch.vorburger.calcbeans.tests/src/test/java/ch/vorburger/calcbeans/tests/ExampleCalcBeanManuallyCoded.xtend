package ch.vorburger.calcbeans.tests

import org.eclipse.xtend.lib.annotations.Accessors

@Accessors  
class ExampleCalcBeanManuallyCoded {
	// TODO remove, once CalcProcessor is fully completed
 
	int a
	def void setA(int a) {
		this.a = a
		c_
	}
	
	int b
	def void setB(int b) {
		this.b = b
		c_
	}
	
	Integer c
	// Supplier<Integer> _c = [ a * b ]
	def private c_() { c = a * b } 
}