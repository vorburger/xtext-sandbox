package ch.vorburger.xtext.sandbox.jvmmodel

import ch.vorburger.xtext.sandbox.learnJvmType.Model
import com.google.inject.Inject
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.common.types.JvmVisibility
import ch.vorburger.xtext.sandbox.learnJvmType.ServiceCall

class LearnJvmTypeJvmModelInferrer extends AbstractModelInferrer {

	@Inject extension JvmTypesBuilder

	def dispatch void infer(Model model, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(model.toClass("demo.Demo")) [
			var i = 0;
			for (element : model.elements) {
				val call = element as ServiceCall
				i = i + 1
				members += call.toField("field" + i, call.type.cloneWithProxies) [
					annotations += Inject.annotationRef()
				]

				val m1 = call.toMethod("executeIntern" + i, inferredType) [
					visibility = JvmVisibility.PRIVATE
					parameters += call.toParameter("it", call.type.cloneWithProxies) => []
					body = call.method
				]
				members += m1

				val ifinal = i
				members += call.toMethod("execute" + i, call.method.inferredType) [
					body = '''
					«IF m1.returnType.qualifiedName != Void.TYPE.typeRef.qualifiedName»return «ENDIF»executeIntern«ifinal»(field«ifinal»);'''
				]
			}
		]
	}
}
