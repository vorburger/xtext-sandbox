package ch.vorburger.xbindings

import org.junit.Test

import static org.junit.Assert.*

class XBindingsXtendTest {
	
	val Property<String> aName = new PropertyImpl<String>();
	val Property<String> bName = new PropertyImpl<String>();
	
	@Test def testBasic() {
		aName.set("world");
		
		bind[| bName.set("hello, " + aName.get) ]
		assertEquals("hello, world", bName.get());
		
		aName.set("Mondo");
		assertEquals("hello, Mondo", bName.get());
	}
	
	// TODO move bind() outside of test
	def bind(()=>void assigner) { 
		PropertyAccessTrackerUtil::ThreadLocal.set( [| assigner.apply() ] )
		try {
			assigner.apply // initial assignment
		} finally {
			PropertyAccessTrackerUtil::ThreadLocal.remove();
		}
	}
 
	// TODO is there any way to write bName.set("hello, " + aName.get) as bName -> aName ?
	
	// TODO consider & test exception handling in assigner 
	
	// TODO think about list bindings
	// TODO think about master detail type bindings
	
	// TODO now write wrappers for EMF and (AOP-enhanced?) Beans and JFace (all using EDB ?), and Vaadin!
	
	// TODO detect infiinite loops
	
	// TODO handle interdependencies
	
	// TODO later, try using xtend Processed Annotations thing for something like @Bean
	 
}