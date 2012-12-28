/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings.beans;

import java.beans.PropertyChangeEvent;

import com.damnhandy.aspects.bean.JavaBean;

import ch.vorburger.xbindings.PropertyChangeListener;
import ch.vorburger.xbindings.PropertyChangeNotifier;

/**
 * Xbindings PropertyChangeNotifier for Java Beans.
 * 
 * @author Michael Vorburger
 */
public class BeanPropertyChangeNotifier implements PropertyChangeNotifier {

	private final JavaBean bean;
	private final String propertyName;

	public BeanPropertyChangeNotifier(JavaBean bean, String propertyName) {
		this.bean = bean;
		this.propertyName = propertyName;  
	}

	@Override
	public void addChangeListener(final PropertyChangeListener cl) {
		bean.addPropertyChangeListener(propertyName, new java.beans.PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				cl.propertyChange();
			}
		});
	}

}
