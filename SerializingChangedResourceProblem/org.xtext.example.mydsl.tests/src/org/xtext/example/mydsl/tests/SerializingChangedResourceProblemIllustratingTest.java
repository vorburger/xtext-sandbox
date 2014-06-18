package org.xtext.example.mydsl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.serializer.ISerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtext.example.mydsl.MyDslInjectorProvider;
import org.xtext.example.mydsl.myDsl.Model;

/**
 * Test case illustrating problem with serializing a resource with
 * unresolved proxies changed programatically (so it has no node model).
 * 
 * Running this test causes the following error on the Console log - but it actually works anyway:

8    [main] ERROR xt.linking.lazy.LazyLinkingResource  - resolution of uriFragment 'xtextLink_::0.0.2::1::/8' failed.
org.eclipse.emf.common.util.BasicEList$BasicIndexOutOfBoundsException: index=2, size=2
	at org.eclipse.emf.common.util.BasicEList.get(BasicEList.java:346)
	at org.eclipse.xtext.linking.lazy.LazyURIEncoder.resolveShortFragment(LazyURIEncoder.java:109)
	at org.eclipse.xtext.linking.lazy.LazyURIEncoder.decode(LazyURIEncoder.java:89)
	at org.eclipse.xtext.linking.lazy.LazyLinkingResource.getEObject(LazyLinkingResource.java:204)
	at org.eclipse.emf.ecore.resource.impl.ResourceSetImpl.getEObject(ResourceSetImpl.java:223)
	at org.eclipse.emf.ecore.util.EcoreUtil.resolve(EcoreUtil.java:202)
	at org.eclipse.emf.ecore.util.EcoreUtil.resolve(EcoreUtil.java:258)
	at org.eclipse.emf.ecore.impl.BasicEObjectImpl.eResolveProxy(BasicEObjectImpl.java:1473)
	at org.xtext.example.mydsl.myDsl.impl.GreetingImpl.getRef(GreetingImpl.java:116)
	at org.xtext.example.mydsl.myDsl.impl.GreetingImpl.eGet(GreetingImpl.java:162)
	at org.eclipse.emf.ecore.impl.BasicEObjectImpl.eGet(BasicEObjectImpl.java:1011)
	at org.eclipse.emf.ecore.impl.BasicEObjectImpl.eGet(BasicEObjectImpl.java:1003)
	at org.eclipse.emf.ecore.impl.BasicEObjectImpl.eGet(BasicEObjectImpl.java:998)
	at org.eclipse.xtext.serializer.sequencer.BacktrackingSemanticSequencer$SerializableObject.<init>(BacktrackingSemanticSequencer.java:146)
	at org.eclipse.xtext.serializer.sequencer.BacktrackingSemanticSequencer.createSequence(BacktrackingSemanticSequencer.java:414)
	at org.xtext.example.mydsl.serializer.MyDslSemanticSequencer.sequence_Greeting(MyDslSemanticSequencer.java:47)
	at org.xtext.example.mydsl.serializer.MyDslSemanticSequencer.createSequence(MyDslSemanticSequencer.java:28)
	at org.eclipse.xtext.serializer.acceptor.SequenceFeeder.acceptEObjectRuleCall(SequenceFeeder.java:299)
	at org.eclipse.xtext.serializer.acceptor.SequenceFeeder.acceptRuleCall(SequenceFeeder.java:325)
	at org.eclipse.xtext.serializer.acceptor.SequenceFeeder.accept(SequenceFeeder.java:239)
	at org.eclipse.xtext.serializer.sequencer.BacktrackingSemanticSequencer.accept(BacktrackingSemanticSequencer.java:396)
	at org.eclipse.xtext.serializer.sequencer.BacktrackingSemanticSequencer.createSequence(BacktrackingSemanticSequencer.java:441)
	at org.xtext.example.mydsl.serializer.MyDslSemanticSequencer.sequence_Model(MyDslSemanticSequencer.java:56)
	at org.xtext.example.mydsl.serializer.MyDslSemanticSequencer.createSequence(MyDslSemanticSequencer.java:34)
	at org.eclipse.xtext.serializer.impl.Serializer.serialize(Serializer.java:85)
	at org.eclipse.xtext.serializer.impl.Serializer.serialize(Serializer.java:108)
	at org.eclipse.xtext.serializer.impl.Serializer.serialize(Serializer.java:122)
	at org.eclipse.xtext.serializer.impl.Serializer.serialize(Serializer.java:51)
	at org.xtext.example.mydsl.tests.SerializingChangedResourceProblemIllustratingTest.test(SerializingChangedResourceProblemIllustratingTest.java:48)
 * 
 * @see TODO bugs.eclipse.org URL ? Or.. actually nothing to report?!
 * 
 * @author Michael Vorburger
 */
@RunWith(XtextRunner.class)
@InjectWith(MyDslInjectorProvider.class)
public class SerializingChangedResourceProblemIllustratingTest {

	private static final String DSL = "Hello a! Hello b! REF a Hello c! REF x";
	
	@Inject ParseHelper<Model> parseHelper;
	@Inject ISerializer serializer;
	
	@Test
	public void test() throws Exception {
		Model model = parseHelper.parse(DSL);
		assertEquals("a", model.getGreetings().get(0).getName());
		assertNull(model.getGreetings().get(0).getRef());
		assertEquals("b", model.getGreetings().get(1).getName());
		assertEquals(model.getGreetings().get(0), model.getGreetings().get(1).getRef());
		assertEquals("c", model.getGreetings().get(2).getName());
		assertTrue(model.getGreetings().get(2).getRef().eIsProxy());
		
		assertEquals(DSL, serializer.serialize(model));
		
		model.getGreetings().remove(1);
		assertEquals("Hello a! Hello c! REF x", serializer.serialize(model));
	}

}
