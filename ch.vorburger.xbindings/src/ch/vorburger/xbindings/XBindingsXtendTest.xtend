package ch.vorburger.xbindings

import org.junit.Test

import static org.junit.Assert.*

class XBindingsXtendTest {
	@Test def testBasic() {
		val Property<String> aName = new PropertyImpl<String>();
		val Property<String> bName = new PropertyImpl<String>();
		aName.set("world");
		
		bind[| bName.set("hello, " + aName.get) ]
		assertEquals("hello, world", bName.get());
		
		aName.set("Mondo");
		assertEquals("hello, Mondo", bName.get());
	}
	
	def bind(()=>void assigner) { 
		PropertyAccessTrackerUtil::ThreadLocal.set( [| assigner.apply() ] )
		try {
			assigner.apply // initial assignment
		} finally {
			PropertyAccessTrackerUtil::ThreadLocal.remove();
		}
	}
 
	// TODO is there any way to write bName.set("hello, " + aName.get) as bName -> aName ?
	
	// TODO later, try using xtend Processed Annotations thing for something like @Bean 
}