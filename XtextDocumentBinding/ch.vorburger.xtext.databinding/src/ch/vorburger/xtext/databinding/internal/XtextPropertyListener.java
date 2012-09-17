/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.SimplePropertyEvent;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.internal.EMFPropertyListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IReadAccess;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

/**
 * Like {@link EMFPropertyListener}, but {@link #addTo(Object)} works with Resource instead of EObject.
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public abstract class XtextPropertyListener extends EContentAdapter implements INativePropertyListener {
	// NOTE extends EContentAdapter, NOT EMFPropertyListener which extends AdapterImpl

	@SuppressWarnings("unchecked")
	protected Resource getResource(Object object) {
		IReadAccess<XtextResource> access = (IReadAccess<XtextResource>) object;
		return access.readOnly(new IUnitOfWork<Resource, XtextResource>() {
			@Override public Resource exec(XtextResource state) throws Exception {
	    		return state;
			}
		});
	}
	
	@Override
	public void addTo(Object source) {
		if (source != null) {
			getResource(source).eAdapters().add(this);
		}
	}

	@Override
	public void removeFrom(Object source) {
		if (source != null) {
			getResource(source).eAdapters().remove(this);
		}
	}

	@Override
	public abstract void notifyChanged(Notification msg);

	protected abstract ISimplePropertyListener getListener();

	protected abstract EStructuralFeature getFeature();

	protected abstract IProperty getOwner();

	
	public abstract static class XtextValuePropertyListener extends XtextPropertyListener {
		@Override
		public void notifyChanged(Notification msg) {
			if (msg.isTouch())
				return;

			// We better make sure it is this property that is affected and not another
			// (Not doing this still passes tests, as it probably gets filter later,
			//  so this is more of an optimization?)
			if (!getFeature().equals(msg.getFeature()))
				return;
			
			System.out.println(msg); // TODO Remove Dev only System.out.println used to learn
			switch (msg.getEventType()) {
			case Notification.REMOVE:
				if (msg.getOldValue() instanceof EObject) {
					EObject oldEObject = (EObject) msg.getOldValue();
					sendChangeEvent(msg, oldEObject.eGet(getFeature()), null);
				}
				break;

			case Notification.ADD:
				if (msg.getNewValue() instanceof EObject) {
					EObject newEObject = (EObject) msg.getNewValue();
					sendChangeEvent(msg, null, newEObject.eGet(getFeature()));
				}
				break;
			
			case Notification.SET:
				sendChangeEvent(msg, msg.getOldValue(), msg.getNewValue());
				break;
				
			case Notification.REMOVE_MANY:
				// Ignore Notification.REMOVE_MANY
				break;
				
			default:
				throw new UnsupportedOperationException(msg.toString());
			}
		}
		
		protected void sendChangeEvent(Notification msg, Object oldValue, Object newValue) {
			getListener().handleEvent(
					new SimplePropertyEvent(SimplePropertyEvent.CHANGE,
							msg.getNotifier(), getOwner(), 
							Diffs.createValueDiff(oldValue, newValue)));
		}
	}
}
