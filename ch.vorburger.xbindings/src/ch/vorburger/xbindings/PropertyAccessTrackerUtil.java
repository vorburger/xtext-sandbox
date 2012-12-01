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
 * ThreadLocal-based utility for PropertyAccessTracker.
 * 
 * @author Michael Vorburger
 */
public class PropertyAccessTrackerUtil {

	public static final ThreadLocal<ChangeListener> ThreadLocal = new ThreadLocal<>();

	public static final PropertyAccessTracker INSTANCE = new PropertyAccessTracker() {
		@Override
		public void accessed(ChangeNotifier cn) {
			cn.setChangeListener(ThreadLocal.get());
		};
	};

	public static void record(final Procedure0 assigner) {
		ThreadLocal.set(new ChangeListener() {
			@Override
			public void changed() {
				assigner.apply();
			}
		});
	}

}
