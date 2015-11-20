package ch.vorburger.calcbeans.tests

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import ch.vorburger.calcbeans.annotations.Calc
import org.junit.Test

class CalcTest {
	
	extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(Calc)
	
	@Test def void testCalc() {
		'''
		import org.eclipse.xtend.lib.annotations.Accessors
		import ch.vorburger.calcbeans.annotations.Calc
		
		@Calc
		@Accessors  
		class ExampleCalcBean {
		
			Integer a
			
			Integer b
			
			// TODO Integer c = a + b
			Integer c = c = a+ b
			
		}
		'''.assertCompilesTo('''
		import ch.vorburger.calcbeans.annotations.Calc;
		import org.eclipse.xtend.lib.annotations.Accessors;
		import org.eclipse.xtext.xbase.lib.Pure;
		
		@Accessors
		@SuppressWarnings("all")
		public class ExampleCalcBean {
		  private Integer a;
		  
		  private Integer b;
		  
		  private Integer c;
		  
		  private void c_() {
		    this.c = Integer.valueOf(((this.a).intValue() + (this.b).intValue()));
		  }
		  
		  @Pure
		  public Integer getA() {
		    return this.a;
		  }
		  
		  public void setA(final Integer a) {
		    this.a = a;
		    c_();
		  }
		  
		  @Pure
		  public Integer getB() {
		    return this.b;
		  }
		  
		  public void setB(final Integer b) {
		    this.b = b;
		    c_();
		  }
		  
		  @Pure
		  public Integer getC() {
		    return this.c;
		  }
		  
		  public void setC(final Integer c) {
		    this.c = c;
		  }
		}
		''')
	}
}