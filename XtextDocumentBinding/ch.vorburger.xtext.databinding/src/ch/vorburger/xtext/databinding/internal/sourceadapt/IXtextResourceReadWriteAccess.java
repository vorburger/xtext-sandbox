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
import org.eclipse.xtext.util.concurrent.IWriteAccess;

/**
 * Combined IReadAccess<XtextResource> & IWriteAccess<XtextResource>.
 * 
 * @author Michael Vorburger
 */
public interface IXtextResourceReadWriteAccess extends IReadAccess<XtextResource>, IWriteAccess<XtextResource> {
	// TODO This could & should be in org.eclipse.xtext (NOT xtext.util *NOR* xtext.ui!), and org.eclipse.xtext.ui.editor.model.IXtextDocument should extend this instead of IReadAccess<XtextResource>, IWriteAccess<XtextResource>  
	
}
