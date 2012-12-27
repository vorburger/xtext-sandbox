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
 * XBindings Java API Sugar.
 * 
 * @author Michael Vorburger
 */
public class XBinding {

	// TODO I'm not entirely sure yet if it's a better design to take Procedure0
	// bindingStatement as constructor argument as it's now, or to instead make
	// XBinding extend Procedure0 (for now, later take it off?) and have
	// an abstract apply() method - like I had it initially.
	//
	public XBinding(Procedure0 bindingStatement) {
		PropertyAccessTrackerUtil.bind(bindingStatement);
	}
	
}
