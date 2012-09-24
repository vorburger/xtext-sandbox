/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.voburger.xtext.sandbox.mydsl.ui.editor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.ui.editor.XtextEditor;

import ch.voburger.xtext.sandbox.mydsl.myDsl.MyDslPackage;
import ch.vorburger.xtext.databinding.XtextProperties;
import ch.vorburger.xtext.databinding.XtextDataBindingContext;

/**
 * Sample Xtext Editor with some SWT Controls next to the edit area which are linked to the Resource via Data Binding.
 * 
 * @author Michael Vorburger, with tips from Kai Kreuzer (for initial tip with
 *         IXtextDocument / link to Jan Köhnlein's Blog) and Yann Andenmatten
 *         (for extends XtextEditor & SashForm; after office working hours)
 */
public class MUIXtextEditor extends XtextEditor {

	// TODO LOW Build a PDE test for this!
	
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
		
		// TODO HIGH Validation Errors should show in the Widget! E.g. Name is ID..
		// http://www.toedter.com/blog/?p=36#comment-16753  ControlDecorationSupport
		// http://www.vogella.com/articles/EclipseDataBinding/article.html#databinding_decorations
		
		final Text modelName = new Text(browserSide, SWT.BORDER);
		final Text childModelName = new Text(browserSide, SWT.BORDER);
		final Text mainGreetingName = new Text(browserSide, SWT.BORDER);
		
		final ListViewer greetingsNames = new ListViewer(browserSide);
		// Instead of the following, we'll use an ViewerSupport below...
		// 		greetingsNames.setContentProvider(new ObservableListContentProvider(/* viewerUpdater */));
		// 		greetingsNames.setLabelProvider(labelProvider);
		// 		greetingsNames.setInput(input);
		
		// TODO HIGH Show Field which gets updated in function of the Selection made in the List (Vogella §2.4 ViewerProperties, also check out ViewersObservables)
		
		// TODO HIGH Is there any use to go via an indirect WritableValue in observe instead of getDocument() ?  Double check if it works.. as we are registering change notification adapters on the Resource in the SourceAccessor implementation, I'm not sure it will work as-is.
		
		DataBindingContext db = new XtextDataBindingContext();
		db.bindValue(
				WidgetProperties.text(SWT.Modify).observe(modelName),
				XtextProperties.value(MyDslPackage.Literals.MODEL__NAME).observe(getDocument()));
		db.bindValue(
				WidgetProperties.text(SWT.Modify).observe(childModelName),
				XtextProperties.value(FeaturePath.fromList(MyDslPackage.Literals.MODEL__CHILD_MODEL, MyDslPackage.Literals.MODEL__NAME))
					.observe(getDocument()));
		db.bindValue(
				WidgetProperties.text(SWT.Modify).observe(mainGreetingName),
				XtextProperties.value(FeaturePath.fromList(MyDslPackage.Literals.MODEL__MAIN_GREEETING, MyDslPackage.Literals.GREETING__NAME))
					.observe(getDocument()));
		
		// TODO Is this bidi? Greeting Name should be able to be changed in List Viewer, and Order up/down modified.. Or is not possible with a List, only with a Table?   
		IObservableList input = XtextProperties.list(MyDslPackage.Literals.MODEL__GREETINGS).observe(getDocument());
		IValueProperty labelProperty = XtextProperties.value(MyDslPackage.Literals.GREETING__NAME);
		// TODO HIGH MAKE THIS MARK?? - ViewerSupport.bind(greetingsNames, input, labelProperty);
		
		sashForm.setWeights(new int[] {10,90});
	}

}
