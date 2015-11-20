package ch.vorburger.calcbeans.annotations

import org.eclipse.xtend.core.macro.declaration.ExpressionImpl
import org.eclipse.xtend.core.macro.declaration.JvmFieldDeclarationImpl
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration
import org.eclipse.xtend.lib.macro.expression.Expression

import static org.eclipse.xtend.lib.macro.declaration.Visibility.*

@Active(CalcProcessor) annotation Calc { }

class CalcProcessor extends AbstractClassProcessor {
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		// Remove @Calc annotation so that xtend-gen/ code does not have any runtime dependency on calcbeans left
		// (? This is important because calcbeans, contrary to other and the standard Xtend active annotation processors,
		// has a dependency to Xtend itself (so that it can work with the AST), but one would typically not want
		// to have Xtend itself on the Classpath of the generated application code. ?)
		// TODO make this removeAnnotation work! the @Calc is still there...
		annotatedClass.removeAnnotation(context.newAnnotationReference(Calc))

		// TODO how-to compose active annotations, and have @Calc imply @Accessors automatically?
		// annotatedClass.addAnnotation(context.newAnnotationReference(Accessors))	
		
		annotatedClass.declaredFields.forEach([field |
			if (field.initializer != null) {
				transformFieldInitializerIntoNewReCalcMethod(field);
				// System.out.println('''«field.simpleName» :: «field.initializer.toString»''')
			}
		])		
	}
	
	def transformFieldInitializerIntoNewReCalcMethod(MutableFieldDeclaration field) {
		field.declaringType.addMethod(field.recalcMethodName) [
			visibility = PRIVATE
			body = field.initializer			

			// IGNORE the following, stemming from earlier confusion... ;-)
			// The https://www.eclipse.org/forums/index.php/m/1715277/ problem
			// is specific to Xbase in a DSL, only; in an Xtend AA it works as-is,
			// because the field.initializer (apparently) already includes the assignment. 
			//			
			// NO! This would be the Xbase source text, instead of the gen. Java code.. 
			// body = '''«field.simpleName» = «field.initializer.toString»;'''
			// TODO Wait for https://bugs.eclipse.org/bugs/show_bug.cgi?id=481992, instead of figuring out how to fix IndexOutOfBoundsException here.. :-(
//			val e = field.initializer.delegate // = (field.initializer as ExpressionImpl).delegate
//			val assignment = XbaseFactory.eINSTANCE.createXAssignment
//			assignment.feature = field.delegate 
//			assignment.value = e
//			body = (field.compilationUnit as CompilationUnitImpl).toExpression(assignment)
			// TODO How to remove the field's initializer.. this doesn't work, because it happens too late, and moving it after [] block doesn't work because then initializer is null during code generation 
			// (OR no need later, as this happens automatically when re-attaching the initializer programmatically above via AST API instead String manipulation) 
			// field.initializer = null as Expression
		]
	}
	
	// TODO These could perhaps be written together more nicely, somehow..
	def getDelegate(Expression element) {
		((element) as ExpressionImpl).delegate
	}
	def getDelegate(MutableFieldDeclaration element) {
		((element) as JvmFieldDeclarationImpl).delegate
	}
	
	def String getRecalcMethodName(MutableFieldDeclaration declaration) {
		declaration.simpleName + '_'
	}
	
}