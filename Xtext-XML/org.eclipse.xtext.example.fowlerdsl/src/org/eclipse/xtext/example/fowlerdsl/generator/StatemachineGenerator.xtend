/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.xtext.example.fowlerdsl.generator

import org.eclipse.emf.ecore.resource.Resource
//import org.eclipse.xtext.example.fowlerdsl.statemachine.Command
//import org.eclipse.xtext.example.fowlerdsl.statemachine.State
//import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import com.google.inject.Inject
import ch.vorburger.xtext.xml.NameURISwapper
import org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl

/**
 * Generator turning DSL Model Resources into XML.
 * This is called by an Xtext Builder.
 * 
 * This is just for quick demo - you may prefer to use the NameURISwapper from your own Builder/Infra instead.
 * 
 * @author Michael Vorburger 
 */
class StatemachineGenerator implements IGenerator {
	
	@Inject NameURISwapper nameURISwapper;
	
	val resourceFactory = new GenericXMLResourceFactoryImpl
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		val root = resource.contents.head
		val rootForXML = nameURISwapper.cloneAndReplaceAllReferencesByNameURIProxies(root)
		
		// This is a hack, it doesn't go through IFileSystemAccess... could do - but that's not the point of the demo, so just: 
		val uri = resource.URI
		val newURI = uri.appendFileExtension("xml")
		val xmlResource = resourceFactory.createResource(newURI)
		xmlResource.getContents().add(rootForXML);
		xmlResource.save(null)
		xmlResource.unload
		
		//fsa.generateFile(resource.className+".java", toJavaCode(resource.contents.head as Statemachine))
	}
	
//	def className(Resource res) {
//		var name = res.URI.lastSegment
//		return name.substring(0, name.indexOf('.'))
//	}
//	
//	def toJavaCode(Statemachine sm) '''
//		import java.io.BufferedReader;
//		import java.io.IOException;
//		import java.io.InputStreamReader;
//		
//		public class «sm.eResource.className» {
//			
//			public static void main(String[] args) {
//				new «sm.eResource.className»().run();
//			}
//			
//			«FOR c : sm.commands»
//				«c.declareCommand»
//			«ENDFOR»
//			
//			protected void run() {
//				boolean executeActions = true;
//				String currentState = "«sm.states.head.name»";
//				String lastEvent = null;
//				while (true) {
//					«FOR state : sm.states»
//						«state.generateCode»
//					«ENDFOR»
//					«FOR resetEvent : sm.resetEvents»
//						if ("«resetEvent.name»".equals(lastEvent)) {
//							System.out.println("Resetting state machine.");
//							currentState = "«sm.states.head.name»";
//							executeActions = true;
//						}
//					«ENDFOR»
//					
//				}
//			}
//			
//			private String receiveEvent() {
//				System.out.flush();
//				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//				try {
//					return br.readLine();
//				} catch (IOException ioe) {
//					System.out.println("Problem reading input");
//					return "";
//				}
//			}
//		}
//	'''
//	
//	def declareCommand(Command command) '''
//		protected void do«command.name.toFirstUpper»() {
//			System.out.println("Executing command «command.name» («command.code»)");
//		}
//	'''
//	
//	def generateCode(State state) '''
//		if (currentState.equals("«state.name»")) {
//			if (executeActions) {
//				«FOR c : state.actions»
//					do«c.name.toFirstUpper»();
//				«ENDFOR»
//				executeActions = false;
//			}
//			System.out.println("Your are now in state '«state.name»'. Possible events are [«
//				state.transitions.map(t | t.event.name).join(', ')»].");
//			lastEvent = receiveEvent();
//			«FOR t : state.transitions»
//				if ("«t.event.name»".equals(lastEvent)) {
//					currentState = "«t.state.name»";
//					executeActions = true;
//				}
//			«ENDFOR»
//		}
//	'''
	
}