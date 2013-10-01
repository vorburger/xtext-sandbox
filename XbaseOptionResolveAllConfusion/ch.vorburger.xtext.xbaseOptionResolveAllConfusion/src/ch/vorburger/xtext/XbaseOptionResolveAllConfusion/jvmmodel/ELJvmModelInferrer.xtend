package ch.vorburger.xtext.XbaseOptionResolveAllConfusion.jvmmodel

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.jdt.annotation.NonNull
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder

/**
 * The IJvmModelInferrer for Vorburger's EL.
 * 
 * Please note that this seems to be required EVEN IF we do not actually want to 
 * gen. complete *.java class extends AbstractExpression source files (the Gen.
 * is not actually currently used; but would now automatically also work thanks
 * to this), just fragments of expression to insert else where.  --  Without this,
 * you'd hit internal problems later, because Xbase (now, new?) appears to expect
 * an IDerivedStateComputer to have run this to create the JvmModel.
 * 
 * The ch.vorburger.el.newpuretests.ELCompilerTest.testNotNull
 * with checkCompilationViaDirectCompilerCall() illustrates this;
 * it used to fail with a 'Couldn't resolve reference to
 * JvmIdentifiableElement '=='.' on XBinaryOperation before this.
 * 
 * @since upgrade of EL to Xtext 2.4.3.
 * 
 * @author Michael Vorburger
 */
class ELJvmModelInferrer extends AbstractModelInferrer {

	// This implementation is "strongly inspired" (mostly copy/pasted)
	// from the org.eclipse.xtext.purexbase.jvmmodel.PureXbaseJvmModelInferrer

	@Inject extension JvmTypesBuilder

   	def dispatch void infer(XExpression e, @NonNull IJvmDeclaredTypeAcceptor acceptor, boolean prelinkingPhase) {
   		acceptor.accept(e.toClass(e.eResource.name)).initializeLater [
   			members += e.toMethod("evaluate", inferredType) [
   				body = e
   			]
   		]
   	}
   	
   	def name(Resource res) {
		val s = res.URI.lastSegment
		return "VorburgerELCompiledExpression_" +  s.substring(0, s.length - '.xbase'.length)
	}
}

