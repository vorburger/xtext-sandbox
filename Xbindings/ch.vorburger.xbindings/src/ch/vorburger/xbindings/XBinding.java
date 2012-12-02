/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xbindings;

import org.eclipse.xtext.xbase.lib.Procedures;

/**
 * XBindings Java API Sugar.
 * 
 * @author Michael Vorburger
 */
public abstract class XBinding {

	public XBinding() {
		XBindings.bind(new Procedures.Procedure0() {
			@Override public void apply() {
				bind();
			}
		});
	}

	abstract public void bind();

}
