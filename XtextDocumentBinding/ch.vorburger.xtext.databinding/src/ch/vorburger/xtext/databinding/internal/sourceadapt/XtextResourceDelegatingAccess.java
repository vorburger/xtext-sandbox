/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal.sourceadapt;

import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IReadAccess;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

// package-local
public class XtextResourceDelegatingAccess implements IXtextResourceReadWriteAccess {
	// TODO Remove this once XTextDocument implements IXtextResourceReadWriteAccess 
	
	private final IWriteAccess<XtextResource> writeDelegate;
	private final IReadAccess<XtextResource> readDelegate;

	@SuppressWarnings("unchecked")
	public XtextResourceDelegatingAccess(IWriteAccess<XtextResource> xTextWriteAccess) {
		writeDelegate = xTextWriteAccess;
		readDelegate = (IReadAccess<XtextResource>) xTextWriteAccess;
	}

	@Override
	public <T> T modify(IUnitOfWork<T, XtextResource> work) {
		return writeDelegate.modify(work);
	}

	@Override
	public <T> T readOnly(IUnitOfWork<T, XtextResource> work) {
		return readDelegate.readOnly(work);
	}
}