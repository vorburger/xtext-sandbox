package org.xtext.example.mydsl.tests;

import javax.inject.Inject;

import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtext.example.mydsl.MyDslInjectorProvider;
import org.xtext.example.mydsl.generator.MyDslGenerator;

@RunWith(XtextRunner.class)
@InjectWith(MyDslInjectorProvider.class)
public class MyDslGeneratorTest {

	@Inject MyDslGenerator generator;
	@Inject JavaIoFileSystemAccess fsa;
	
	@Test
	public void testDoGenerate() {
		fsa.setOutputPath(IFileSystemAccess.DEFAULT_OUTPUT, ".");
		generator.doGenerate(null, fsa);
	}

}
