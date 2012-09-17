/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.tests.utils;

import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IReadAccess;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

/**
 * IXtextResourceReadWriteAccess for tests.
 *
 * @author Michael Vorburger
 */
public class XtextResourceTestAccess implements IReadAccess<XtextResource>, IWriteAccess<XtextResource> {

	private final XtextResource state;

	public XtextResourceTestAccess(XtextResource state) {
		super();
		this.state = state;
	}

	@Override
	public <T> T readOnly(IUnitOfWork<T, XtextResource> work) {
		try {
			return work.exec(state);
		} catch (Exception e) {
			throw new WrappedException(e);
		}
	}

	@Override
	public <T> T modify(IUnitOfWork<T, XtextResource> work) {
		try {
			return work.exec(state);
		} catch (Exception e) {
			throw new WrappedException(e);
		}
	}

}
