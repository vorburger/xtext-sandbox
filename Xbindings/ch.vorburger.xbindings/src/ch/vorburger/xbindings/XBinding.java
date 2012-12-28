/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings;

import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;

/**
 * XBindings.
 * 
 * @author Michael Vorburger
 */
public class XBinding implements PropertyChangeListener { // TODO rename to XPropertyBinding or XValueBinding? 
	@Override
	public void propertyChange() {
		this.apply();
	}

	private static final ThreadLocal<PropertyChangeListener> currentThreadPropertyChangeListener = new ThreadLocal<>();

	public static final PropertyAccessListener PROPERTY = new PropertyAccessListener() {
		@Override
		public void accessed(PropertyChangeNotifier cn) {
			final PropertyChangeListener propertyChangeListener = currentThreadPropertyChangeListener.get();
			if (propertyChangeListener != null)
				cn.addChangeListener(propertyChangeListener);
		};
	};

	// TODO Later instead of using xtext.xbase.Procedure0 could use our own interface, just so that we don't depend on xtext just for this 
	protected final Procedure0 binding;

	// TODO I'm not entirely sure yet if it's a better design to take Procedure0
	// bindingStatement as constructor argument as it's now, or to instead make
	// XBinding extend Procedure0 and have an abstract apply() method 
	// (like I had it initially).
	//
	public XBinding(final Procedure0 bindingStatement) {
		binding = bindingStatement;
		apply(); // initial assignment
	}

	// TODO doc! yada yada yada... applies the bindingStatement closure passed to constructor... records any accessors... 
	protected void apply() {
		// This check is NOT NEEDED (causes a problem in fact; try it)
		// Not doing this and missing this use case would only be an issue in real world
		// if a Binding contained two statements, with the first an assignment/setter and the second an accessor (would be missed)
		// TODO Planned Refactoring to have Binding more clearly express Property to set and expression used to obtain its (one and only) value, thus avoiding Bindings with two Statements conceptually 
//		if (currentThreadPropertyChangeListener.get() != null) {
//			throw new IllegalStateException("how come threadLocal is already set?!");
//		}
		
		currentThreadPropertyChangeListener.set(this);
		try {
			binding.apply();
		// should we catch(Throwable) & log in case of trouble? but as it nicely bubbles up, there is currently no need to do that..
		// if Exception is caught, could remove/dispose binding here - but we would have to keep track of all PropertyChangeNotifiers seen for that
		} finally {
			currentThreadPropertyChangeListener.remove();
		}
	}
	
}
