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
 * ThreadLocal-based utility for PropertyChangeListener.
 * 
 * @author Michael Vorburger
 */
public class PropertyAccessTrackerUtil { // TODO rename class.. what would be a better name?

	private static final ThreadLocal<PropertyChangeListener> threadLocal = new ThreadLocal<>();

	public static final PropertyAccessListener INSTANCE = new PropertyAccessListener() {
		@Override
		public void accessed(PropertyChangeNotifier cn) {
			if (threadLocal.get() != null)
				cn.addChangeListener(threadLocal.get());
		};
	};

	public static void bind(final Procedure0 assigner) {
		if (threadLocal.get() != null) {
			throw new IllegalStateException("how come threadLocal is already set?!");
		}
		
		// PropertyAccessTrackerUtil::ThreadLocal.set( [| assigner.apply() ] )
		threadLocal.set(new PropertyChangeListener() {
			@Override
			public void changed() {
				assigner.apply();
			}
		});
		
		try {
			assigner.apply(); // initial assignment
		// could catch(Throwable) & log in case of trouble with initial assignment, but as it nicely bubbles up, there is currently no need to do that
		// if Exception is caught, could remove/dispose binding here - but we would have to keep track of all PropertyChangeNotifiers seen for that
		} finally {
			threadLocal.remove();
		}
	}

}
