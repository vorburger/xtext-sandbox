/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings.beans;

import java.lang.reflect.Field;

import org.aspectj.lang.reflect.FieldSignature;

import ch.vorburger.xbindings.PropertyChangeNotifier;
import ch.vorburger.xbindings.XBinding;

import com.damnhandy.aspects.bean.Observable;
import com.damnhandy.aspects.bean.JavaBean;

/**
 * Aspect which makes all getters call the PropertyAccessTrackerUtil.
 * 
 * @author Michael Vorburger
 */
public aspect GetterTrackingAspect {

	// TODO support boolean isXYZ() as well!
	
	pointcut getters(JavaBean bean): 
		target(bean) && get(* (@Observable *).*) && withincode(* @Observable *.get*(..))
		// && !withinNewObservable() 
		// && !set(@Silent * (@Observable *).*)
		;
	
	Object around(JavaBean bean) : getters(bean) {
		FieldSignature fieldSig = (FieldSignature) thisJoinPoint.getSignature();
		Field field = fieldSig.getField();
		String fieldName = field.getName();

		PropertyChangeNotifier propertyChangeNotifier = new BeanPropertyChangeNotifier(bean, fieldName);
		XBinding.PROPERTY.accessed(propertyChangeNotifier);
		return proceed(bean);
    }
}
