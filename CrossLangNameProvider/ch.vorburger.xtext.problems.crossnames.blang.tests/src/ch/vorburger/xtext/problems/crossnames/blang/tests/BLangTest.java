package ch.vorburger.xtext.problems.crossnames.blang.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipselabs.xtext.utils.unittesting.XtextRunner2;
import org.eclipselabs.xtext.utils.unittesting.XtextTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.vorburger.xtext.problems.crossnames.bLang.ModelB;

@InjectWith(BLangWithADependencyInjectorProvider.class)
@RunWith(XtextRunner2.class)
public class BLangTest extends XtextTest {

    @Test public void testALang() throws Exception {
        testFile("a.alang");
    }

    @Test public void testBLang() throws Exception {
        ignoreFormattingDifferences(); // TODO how to adapt formatter to not insert whitespaces?
        testFile("b.blang", "a.alang");
        ModelB modelB = (ModelB) getModelRoot();
        assertEquals("Mondpacxo", modelB.getModelA().getName());
    }

    @Test public void testB2Lang() throws Exception {
        ignoreFormattingDifferences(); // TODO how to adapt formatter to not insert whitespaces?
        testFile("b2.blang", "a.alang");
    }
}
