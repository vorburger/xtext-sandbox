package org.xtext.example.mydsl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtext.example.mydsl.MyDslInjectorProvider;
import org.xtext.example.mydsl.person.Gender;
import org.xtext.example.mydsl.person.Person;

@RunWith(XtextRunner.class)
@InjectWith(MyDslInjectorProvider.class)
public class DefaultValuesTest {

	@Inject ParseHelper<Person> parser;
	
	@Test
	public void testDefaultValues() throws Exception {
		Person emptyPerson = parser.parse("PersonName \"Satish\" { }");
		assertNull(emptyPerson.getAge());
		assertNull(emptyPerson.getStatus());
		// NOT POSSIBLE, Ecore limitation: 
		// assertNull(emptyPerson.getGender());
		assertEquals(Gender.UNKNOWN, emptyPerson.getGender());
	}

}
