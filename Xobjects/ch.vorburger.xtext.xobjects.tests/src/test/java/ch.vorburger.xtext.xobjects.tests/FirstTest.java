public class FirstTest {

	@Test
	public testStatic() {
		Book object = First.new();
		check(object);
		assertThat(object.getTitle(), is("Core Java Data Objects"));
		assertThat(object.getPages(), is(1234));
	}

	@Test
	public testDynXObjectStaticEClass() {
		Book object = new XObjectsReader("/simple/first.xobject", First.class);
		assertThat(object.getTitle(), is("Core Java Data Objects"));
		assertThat(object.getPages(), is(1234));
	}

	@Test
	public testDynXObjectDynEClass() {
		EObject object = new XObjectsReader("/simple/first.xobject", EObject.class);
		assertThat(object.eGet("title"), is("Core Java Data Objects"));
		assertThat(object.eGet("pages"), is(1234));
	}

}
