calcbeans
=========

_NOTE to posteriority: I got stuck with the Xtend AA approach, and have continued this idea within my Xeemim Xbase-based DSL instead, for now... ;-)_

Blurb for Google search hits: Java beans backed by Excel like formulae, calculating property dependency updates, à la data binding for automated change notification tracking of dependant fields, with very clean readable business friendly source code syntax, completely static typed code without any EL type Strings, thus fully IDE supported, including circular reference cycle error detection at compile time;

based on Xtend active annotations,

by Michael Vorburger.

```java
@Calc @Accessors class Bean {

    Integer a

    Integer b

    Integer c = a * b // The magic @Calc turns this into MUCH more than a class Java static field initializer...
}
```

TODO
----

* working v1... ;-)
* proper field dependency..
* bi-directional binding (fine, as long as no cycles)
* compile time validation for cycles
* Collection (List etc) support, see ch.vorburger.calcbeans.demo.Order
** which really raises the larger topic of binding into more than 1 object, doesn't it?
* null safe math operations ?+ like ?. or perhaps via elvis ?: or no, should be possible via null-safe DoubleExtensions sub?
* merge calcbeans.tests (back) into calcbeans - actually no need?
* UI demo? E.g. Vaadin...
* more tests as in https://github.com/vorburger/xtext-sandbox/tree/master/Xbindings/ch.vorburger.xbindings/src/ch/vorburger/xbindings/tests


Similar stuff
-------------

* https://www.formulacompiler.org
* http://jxls.sourceforge.net
* https://github.com/vorburger/xtext-sandbox/tree/master/Xbindings - a 3 yrs earlier stab by @vorburger at the very same problem, with two different takes; instead of Xtend Active Annotation, a JavaFX-like Property<T> and an AspectJ AOP-based approach.

