package org.eclipse.xtext.example.fowlerdsl.serializer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.example.fowlerdsl.services.StatemachineGrammarAccess;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;

import ch.vorburger.xtext.xml.NameURISwapperImpl;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class StatemachineSemanticSequencer extends AbstractStatemachineSemanticSequencer {
	
	@Inject
	private StatemachineGrammarAccess grammarAccess;

// TODO remove - this is useless, cannot work..	
//	@Override
//	protected void sequence_Statemachine(EObject context, Statemachine semanticObject) {
//		// TODO Some refactoring + Share as much as possible of this code with NameURISwapperImpl
//		if (semanticObject.eIsProxy()) {
//	        final URI uri = EcoreUtil.getURI(semanticObject);			
//	        if (NameURISwapperImpl.isNameScheme(uri)) {
//	    		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
//	    		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
//                final String crossRefString = uri.path().substring(1); // TODO Share this code with NameURISwapperImpl 
//	    		feeder.accept(grammarAccess.getStatemachineAccess().getNameIDTerminalRuleCall_2_0(), crossRefString ); 
//	    		feeder.finish();
//	    		return;
//	        }
//		}
//		super.sequence_Statemachine(context, semanticObject);
//	}
	
}
