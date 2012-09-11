/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.xml.example.fowlerdsl.tests

import com.google.inject.Inject
import org.eclipse.xtext.example.fowlerdsl.StatemachineInjectorProvider
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import static org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import ch.vorburger.xtext.xml.NameURISwapper
import ch.vorburger.xtext.xml.EIO
import org.eclipse.emf.common.util.URI
import java.io.IOException
import com.google.common.io.Files
import java.io.File
import java.nio.charset.Charset

/**
 * Tests for example.fowlerdsl with ch.vorburger.xtext.xml.
 * 
 * @author Michael Vorburger
 */
@RunWith(typeof(XtextRunner))
@InjectWith(typeof(StatemachineInjectorProvider))
class FowlerDslXmlTest2 {
	 
	@Inject extension ParseHelper<Statemachine>
	@Inject extension ValidationTestHelper
	
	@Inject NameURISwapper nameURISwapper;
	@Inject EIO eio;
	
	@Test
	def void testWriteFowlerDslAsXML() {
		val model = sampleStatemachineText.parse
		model.assertNoErrors
		assertEquals("sample1", model.name)
		assertEquals("event1", model.events.get(0).name)
		
		val modelForXML = nameURISwapper.cloneAndReplaceAllReferencesByNameURIProxies(model);
		val uri = URI::createFileURI("new-sample.xml")
		eio.cloneAndSave(uri, modelForXML)
		assertFileEquals("base-sample.xml", "new-sample.xml")		
	}

	@Test
	def void testReadFowlerDslAsXMLWriteDSL() {
		val uri = URI::createFileURI("new-sample.xml")
		val xmlModel = eio.load(uri)
		nameURISwapper.replaceAllNameURIProxiesByReferences(xmlModel)
		eio.cloneAndSave(URI::createFileURI("new-sample.statemachine"), xmlModel);

		val newSampleStatemachine = loadFile("new-sample.statemachine")
		assertEquals(sampleStatemachineText.collapseWhitespace, newSampleStatemachine)		
		    
		// Make sure that it was really cloned (to be safe), so XML Resource still contains XML model
        assertTrue(xmlModel.eResource.getContents().contains(xmlModel));
	}

	@Test
	def void testReadFowlerDslAsXMLWithNonExistingCrossRefWriteDSL() {
		// We load a *.statemachine here, without needing its result
		// so that it's in the ResourceSet and that cross refs work
		eio.load(URI::createFileURI("new-sample.statemachine"))
		
		val xmlModel = eio.load(URI::createFileURI("new-sample-badcrossref.xml"))
		nameURISwapper.replaceAllNameURIProxiesByReferences(xmlModel)
		eio.cloneAndSave(URI::createFileURI("new-sample-badcrossref.statemachine"), xmlModel);

		val newSampleStatemachine = loadFile("new-sample-badcrossref.statemachine")
		assertEquals(sampleBadCrossRefStatemachineText.collapseWhitespace, newSampleStatemachine)		
	}
	def String sampleBadCrossRefStatemachineText() { 
		'''
			name: sampleBadCrossRef
			friends: sample1
		'''
	}

	
    def void assertFileEquals(String expectedFilePath, String actualFilePath) throws IOException {
        assertEquals(loadFile(expectedFilePath), loadFile(actualFilePath));
    }

	def String loadFile(String filePath) {
		return Files::toString(new File(filePath), Charset::defaultCharset).collapseWhitespace;
	}
	
	def String collapseWhitespace(String string) {
		return string.replaceAll("\\s+", " ").trim()
	}
	
	def String sampleStatemachineText() {
		'''
			name: sample1

			events
				event1 CodeOfEvent1
				event2 CodeOfEvent2
			end
			
			resetEvents 
				event1
			end
			
			commands
				command1 CodeOfCommand1
				command2 CodeOfCommand2
			end
			
			state state1 
				actions { command1 command2 }
				event1 => state1
				event2 => state2
			end
			
			state state2
			end
		'''		
	}
}