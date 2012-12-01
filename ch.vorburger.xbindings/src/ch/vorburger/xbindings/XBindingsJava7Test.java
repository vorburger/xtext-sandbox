package ch.vorburger.xbindings;

import static org.junit.Assert.assertEquals;

import org.eclipse.xtext.xbase.lib.Procedures;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.junit.Test;

/**
 * An idea.
 * 
 * @author Michael Vorburger
 */
public class XBindingsJava7Test {

	// TODO try to write this with Java v8 lambda expressions (closures) !
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=380188

	Property<String> aName = new PropertyImpl<String>();
	Property<String> bName = new PropertyImpl<String>();
	
	@Test
	public void testBasic() {
		aName.set("Yuhu");
		
		final Procedure0 assigner = new Procedures.Procedure0() {
			@Override public void apply() {
				bName.set("hello, " + aName.get());
			}
		};
		PropertyAccessTrackerUtil.ThreadLocal.set(new ChangeListener() {
			@Override public void changed() { 
				assigner.apply(); }
		});
		try {
			assigner.apply();
		} finally {
			PropertyAccessTrackerUtil.ThreadLocal.remove();
		}
		assertEquals("hello, Yuhu", bName.get());
		
		aName.set("Yoho");
		assertEquals("hello, Yoho", bName.get());
	}
}
