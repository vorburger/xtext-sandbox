package ch.vorburger.xbindings;

interface Property<T> extends ChangeNotifier {
	T get();
	void set(T newValue);
}