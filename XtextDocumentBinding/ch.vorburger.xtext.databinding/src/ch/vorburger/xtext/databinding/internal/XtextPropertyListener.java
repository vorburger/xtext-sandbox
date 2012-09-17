/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal;

import java.util.List;

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
import org.eclipse.xtext.resource.XtextSyntaxDiagnostic;
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
	protected Resource getResource(Object source) {
		// @see very similar logic in EMFXtextValueProperty.doGetValue() :
		if (source instanceof IReadAccess<?>) {
			IReadAccess<XtextResource> access = (IReadAccess<XtextResource>) source;	
			return access.readOnly(new IUnitOfWork<Resource, XtextResource>() {
				@Override public Resource exec(XtextResource state) throws Exception {
		    		return state;
				}
			});
		} else if (source instanceof Resource) {
			return (Resource) source;
		} else {
			throw new IllegalArgumentException("Observed source object is neither an IReadAccess<XtextResource> nor an EMF Resource: " + source);
		}
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
			System.out.println(msg); // TODO Remove Dev only System.out.println used to learn

			if (msg.isTouch())
				return;
			
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
				// We better make sure it is this property that is affected and not another
				// (Not doing this still passes tests, as it probably gets filter later,
				//  so this is more of an optimization?)
				if (!getFeature().equals(msg.getFeature()))
					return;
				sendChangeEvent(msg, msg.getOldValue(), msg.getNewValue());
				break;
				
			case Notification.REMOVE_MANY:
				// Ignore Notification.REMOVE_MANY
				break;
			case Notification.ADD_MANY:
				@SuppressWarnings("unchecked") List<Object> newValue = (List<Object>) msg.getNewValue();
				if (newValue != null & !newValue.isEmpty()) {
					if (newValue.get(0) instanceof XtextSyntaxDiagnostic) {
						// Ignore Notification.ADD_MANY for Diagnostics		
					} else {
						throw new UnsupportedOperationException(msg.toString());
					}
				}
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
