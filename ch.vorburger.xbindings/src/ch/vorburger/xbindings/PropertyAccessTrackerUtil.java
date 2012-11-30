package ch.vorburger.xbindings;

public class PropertyAccessTrackerUtil {

	public static final ThreadLocal<ChangeListener> ThreadLocal = new ThreadLocal<>();

	public static final PropertyAccessTracker INSTANCE = new PropertyAccessTracker() {
		@Override
		public void accessed(ChangeNotifier cn) {
			cn.setChangeListener(ThreadLocal.get());
		};
	};


}
