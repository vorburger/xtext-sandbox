package ch.vorburger.xtext.sandbox.scoping

import ch.vorburger.xtext.sandbox.learnJvmType.ServiceCall
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.util.SimpleAttributeResolver
import org.eclipse.xtext.xbase.XbasePackage

class LearnJvmTypeScopeProvider extends AbstractLearnJvmTypeScopeProvider {

	public final static SimpleAttributeResolver<EObject, String> SIMPLENAME_RESOLVER = SimpleAttributeResolver.newResolver(String, "simpleName")

	def dispatch getDispatchedScope(ServiceCall context, EReference reference) {
		// NOTE: context is NOT a XFeatureCall but already the ServiceCall ..
		if (reference == XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE) {
			val jvmDeclaredType = context.type.type as JvmDeclaredType
			// TODO This probably does not allow methods in parent classes - OK to use OverrideHelper here in a ScopeProvider? TDD it.
			val ops = jvmDeclaredType.declaredOperations
			return Scopes.scopeFor(ops, QualifiedName.wrapper(SIMPLENAME_RESOLVER), IScope.NULLSCOPE)
		}
		super.getScope(context, reference)
	}

	def dispatch getDispatchedScope(EObject context, EReference reference) {
		super.getScope(context, reference)
	}

	// This is required for proper handling of null context - 'dispatch' doesn't do the right thing otherwise
	override getScope(EObject context, EReference reference) {
		if (context == null || context.eResource() == null || context.eResource().getResourceSet() == null) {
			return IScope.NULLSCOPE;
		}
		getDispatchedScope(context, reference)
	}

}
