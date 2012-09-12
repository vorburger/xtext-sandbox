/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.voburger.xtext.sandbox.mydsl.ui.editor;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import ch.voburger.xtext.sandbox.mydsl.myDsl.Greeting;
import ch.voburger.xtext.sandbox.mydsl.myDsl.Model;
import ch.voburger.xtext.sandbox.mydsl.myDsl.MyDslPackage;
import ch.vorburger.xtext.databinding.EMFXtextProperties;

/**
 * Xtext Editor with a Browser in it.
 * 
 * @author Michael Vorburger, with tips from Kai Kreuzer (for initial tip with
 *         IXtextDocument / link to Jan Köhnlein's Blog) and Yann Andenmatten
 *         (for extends XtextEditor & SashForm; after office working hours)
 */
public class MUIXtextEditor extends XtextEditor {

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout (new FillLayout());

		SashForm sashForm = new SashForm(container,SWT.VERTICAL);
		sashForm.setLayout(new FillLayout());

		Composite xtextSide = new Composite(sashForm,SWT.NONE);
		xtextSide.setLayout(new FillLayout());
		super.createPartControl(xtextSide);
		
		Composite browserSide = new Composite(sashForm,SWT.NONE);
		browserSide.setLayout(new FillLayout());
		
		final Text textWidgetWithListener = new Text(browserSide, SWT.BORDER);
		textWidgetWithListener.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				final String text = textWidgetWithListener.getText();
				//System.out.println(text);
				getDocument().modify(new IUnitOfWork.Void<XtextResource>() {
					@Override
					public void process(XtextResource state) throws Exception {
						Model model = (Model) state.getContents().get(0);
						model.getGreetings().get(0).setName(text);
					}
				});
			}
		});
		
		final Text textWidgetWithBinding = new Text(browserSide, SWT.BORDER);

		// TODO This is wrong! exec() Doc: "Do not return any references to something contained in this resource" - but then how to do data binding?!
		// Note XtextDocument's ensureThatStateIsNotReturned - which would catch this, if not commented out? Ask on Forum why not active, create Bug?
		Greeting greeting = getDocument().readOnly(new IUnitOfWork<Greeting, XtextResource>() {
			public Greeting exec(XtextResource state) throws Exception {
				Model model = (Model) state.getContents().get(0);
				return model.getGreetings().get(0);
			};
		});
		
		EMFDataBindingContext db = new EMFDataBindingContext();
		db.bindValue(
				WidgetProperties.text(SWT.Modify).observe(textWidgetWithBinding),
				EMFXtextProperties.value(getDocument(), MyDslPackage.Literals.GREETING__NAME).observe(greeting));
		
		sashForm.setWeights(new int[] {90,10});
	}

}
