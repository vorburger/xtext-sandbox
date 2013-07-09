package ch.vorburger.xtext.serializerMultiValueBug;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.serializer.ISerializer;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.vorburger.xtext.SerializerMultiValueBugInjectorProvider;
import ch.vorburger.xtext.serializerMultiValueBug.impl.SerializerMultiValueBugFactoryImpl;

/**
 * Test Case illustrating Bug in Serializer.
 *
 * @author Michael Vorburger
 */
@RunWith(XtextRunner.class)
@InjectWith(SerializerMultiValueBugInjectorProvider.class)
public class SerializerMultiValueBugTest {

	// @Inject ParseHelper<Model> parser;
	@Inject ISerializer serializer;
	
	@Test
	public void testSerializerMultiValueBug() {
		Model model = SerializerMultiValueBugFactoryImpl.eINSTANCE.createModel();
		Greeting greeting = SerializerMultiValueBugFactoryImpl.eINSTANCE.createGreeting();
		greeting.setName("Satish");
		greeting.getAttributes().add("Lausanne/Switerland");
		greeting.getAttributes().add("Bangalore/India");
		model.getGreetings().add(greeting);
		
		String text = serializer.serialize(model);
		assertEquals("Hello Satish attributes: \"Lausanne/Switerland\" ; \"Bangalore/India\"", text);
	}

}
