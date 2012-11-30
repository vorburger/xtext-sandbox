package ch.vorburger.xbindings;

import static org.junit.Assert.assertEquals;

import org.eclipse.xtext.xbase.lib.Procedures;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.junit.Ignore;
import org.junit.Test;

/**
 * An idea.
 * 
 * @author Michael Vorburger
 */
public class XBindingsTest {

	// TODO not sure yet where exactly this will live in the end..  
	static final ThreadLocal<ChangeListener> currentChangeListener = new ThreadLocal<>();

	// TODO not sure yet where exactly this will live.. later ThreadLocal<PropertyAccessTracker> ? 
	static final PropertyAccessTracker pat = new PropertyAccessTracker() {
		@Override public void accessed(ChangeNotifier cn) { cn.setChangeListener(currentChangeListener.get()); };
	};
	
	// could be EMF, AOP-enhanced bean, EDB API, V Item, ...
	static class A {
		Property<String> name = new PropertyImpl<String>();
	}
	
	@Test
	public void testBasic() {
		final A a = new A();
		a.name.set("Yuhu");
		final Property<String> bName = new PropertyImpl<String>();
		
		final Procedure0 assigner = new Procedures.Procedure0() {
			@Override public void apply() {
				bName.set("hello, " + a.name.get());
			}
		};
		currentChangeListener.set(new ChangeListener() {
			@Override public void changed() { 
				assigner.apply(); }
		});
		assigner.apply();
		// TODO later probably better do this inside a finally
		currentChangeListener.remove();
		assertEquals("hello, Yuhu", bName.get());
		
		a.name.set("Yoho");
		assertEquals("hello, Yoho", bName.get());
	}

	// TODO now rewrite it in xtend.. ;)
	
	@Test
	@Ignore
	public void testBidi() {
// TODO bidi		
//		bName.set("booba");
//		assertEquals("booba", a.name.get());
	}
	
	// TODO think about list bindings
	// TODO think about master detail type bindings
	
	// TODO now write wrappers for EMF and Beans (using EDB ?)
	
	// TODO detect inifinite loops
	
	// TODO handle interdependencies
}
