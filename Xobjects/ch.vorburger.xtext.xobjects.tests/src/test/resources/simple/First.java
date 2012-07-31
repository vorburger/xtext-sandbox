// NOTE: Gen. this Java is completely optional - see FirstTest for dynamic variant

@Generated
public class First {
	// gen. code should, ideally, unless there is a good reason to, NOT implement or extend some RT FMK ObjectFactory<First> kind of thing

	public First new() {
		return new(new First());		
	}

	// This variant is useful if you must control the object instation (e.g. because you do DI)
	public void new(First object) {
		object.setTitle("Core Java Data Objects");
		object.setPages(1234);
	}

}
