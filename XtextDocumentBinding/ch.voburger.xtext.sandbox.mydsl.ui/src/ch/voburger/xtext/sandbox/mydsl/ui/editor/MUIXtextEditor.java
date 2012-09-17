/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.voburger.xtext.sandbox.mydsl.ui.editor;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.ui.editor.XtextEditor;

import ch.voburger.xtext.sandbox.mydsl.myDsl.MyDslPackage;
import ch.vorburger.xtext.databinding.EMFXtextProperties;

/**
 * Sample Xtext Editor with some SWT Controls next to the edit area which are linked to the Resource via Data Binding.
 * 
 * @author Michael Vorburger, with tips from Kai Kreuzer (for initial tip with
 *         IXtextDocument / link to Jan Köhnlein's Blog) and Yann Andenmatten
 *         (for extends XtextEditor & SashForm; after office working hours)
 */
public class MUIXtextEditor extends XtextEditor {

	// TODO Build a PDE test for this!
	
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout (new FillLayout());

		SashForm sashForm = new SashForm(container,SWT.VERTICAL);
		sashForm.setLayout(new FillLayout());

		Composite browserSide = new Composite(sashForm,SWT.NONE);
		browserSide.setLayout(new FillLayout());

		Composite xtextSide = new Composite(sashForm,SWT.NONE);
		xtextSide.setLayout(new FillLayout());
		super.createPartControl(xtextSide);
		
		// TODO Validation Errors should show in the Widget! E.g. Name is ID..
		// http://www.toedter.com/blog/?p=36#comment-16753  ControlDecorationSupport
		// http://www.vogella.com/articles/EclipseDataBinding/article.html#databinding_decorations
		
		final Text modelName = new Text(browserSide, SWT.BORDER);
		final Text childModelName = new Text(browserSide, SWT.BORDER);
		final Text mainGreetingName = new Text(browserSide, SWT.BORDER);
		
		EMFDataBindingContext db = new EMFDataBindingContext();
		db.bindValue(
				WidgetProperties.text(SWT.Modify).observe(modelName),
				EMFXtextProperties.value(MyDslPackage.Literals.MODEL__NAME).observe(getDocument()));
		db.bindValue(
				WidgetProperties.text(SWT.Modify).observe(childModelName),
				EMFXtextProperties.value(FeaturePath.fromList(MyDslPackage.Literals.MODEL__CHILD_MODEL, MyDslPackage.Literals.MODEL__NAME))
					.observe(getDocument()));
		db.bindValue(
				WidgetProperties.text(SWT.Modify).observe(mainGreetingName),
				EMFXtextProperties.value(FeaturePath.fromList(MyDslPackage.Literals.MODEL__MAIN_GREEETING, MyDslPackage.Literals.GREETING__NAME))
					.observe(getDocument()));
		
		sashForm.setWeights(new int[] {10,90});
	}

}
