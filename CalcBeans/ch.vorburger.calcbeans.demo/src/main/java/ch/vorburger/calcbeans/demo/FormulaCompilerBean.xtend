package ch.vorburger.calcbeans.demo

import ch.vorburger.calcbeans.annotations.Calc
import org.eclipse.xtend.lib.annotations.Accessors

import static java.lang.Math.*

@Calc @Accessors class FormulaCompilerBean {
	
	double customerRebate
	double articleRebate
	CustomerCategory customerCategory
	
	double rebate = if (customerCategory == CustomerCategory.A ) customerRebate + articleRebate else max(customerRebate, articleRebate)
}

enum CustomerCategory {
	A, B, C
}