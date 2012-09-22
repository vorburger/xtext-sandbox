/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.databinding.internal;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.SimplePropertyEvent;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.internal.EMFPropertyListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.xtext.resource.XtextSyntaxDiagnostic;

import ch.vorburger.xtext.databinding.internal.sourceadapt.SourceAccessor;

/**
 * Like {@link EMFPropertyListener}, but {@link #addTo(Object)} works with Resource instead of EObject.
 * 
 * @author Michael Vorburger
 * @author Phani Kumar (@netsrujana) - Intro. XtextListPropertyListener (in collaboration with Michael Vorburger)
 */
@SuppressWarnings("restriction")
public abstract class XtextPropertyListener extends EContentAdapter implements INativePropertyListener {
	// NOTE This extends EContentAdapter, NOT EMFPropertyListener which extends just AdapterImpl

	@Override
	public void addTo(Object source) {
		SourceAccessor sourceAccessor = (SourceAccessor) source;
		sourceAccessor.addAdapter(this);
	}

	@Override
	public void removeFrom(Object source) {
		SourceAccessor sourceAccessor = (SourceAccessor) source;
		sourceAccessor.removeAdapter(this);
	}

	@Override
	public abstract void notifyChanged(Notification msg);

	protected abstract ISimplePropertyListener getListener();

	protected abstract EStructuralFeature getFeature();

	protected abstract IProperty getOwner();

	
	public abstract static class XtextValuePropertyListener extends XtextPropertyListener {
		@Override
		public void notifyChanged(Notification msg) {
			// TODO HIGH Remove Dev only System.out.println used to learn.. use a logger?
			System.out.println("XtextValuePropertyListener: " + msg);

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
				// In an ideal world, we should may be "filter" to make sure it is this property
				// that is affected and not another.  However, as not doing that still works,
				// as something else appears to re-check further down the road, we pass on
				// everything... because AFAIK it would be difficult to get this right:
				// In the case of a nested property (based on an observeDetail()),
				// I can't see how we could correctly filter? 
				//
				// It's possibly slightly in-efficient, but it works!
				// Could optimize later.
				//
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

	public abstract static class XtextListPropertyListener extends XtextPropertyListener {
		@Override
		public void notifyChanged(Notification msg) {
			// TODO HIGH Remove Dev only System.out.println used to learn.. use a logger?
			System.out.println("XtextListPropertyListener: " + msg);

			// TODO VERY HIGH getFeature() == msg.getFeature()
			if (getFeature() == msg.getFeature() && !msg.isTouch()) {
				final ListDiff diff;
				switch (msg.getEventType()) {
				case Notification.ADD: {
					diff = Diffs.createListDiff(Diffs.createListDiffEntry(
							msg.getPosition(), true, msg.getNewValue()));
					break;
				}
				case Notification.ADD_MANY: {
					Collection<?> newValues = (Collection<?>) msg.getNewValue();
					ListDiffEntry[] listDiffEntries = new ListDiffEntry[newValues
							.size()];
					int position = msg.getPosition();
					int index = 0;
					for (Object newValue : newValues) {
						listDiffEntries[index++] = Diffs.createListDiffEntry(
								position++, true, newValue);
					}
					diff = Diffs.createListDiff(listDiffEntries);
					break;
				}
				case Notification.REMOVE: {
					diff = Diffs.createListDiff(Diffs.createListDiffEntry(
							msg.getPosition(), false, msg.getOldValue()));
					break;
				}
				case Notification.REMOVE_MANY: {
					Collection<?> oldValues = (Collection<?>) msg.getOldValue();
					ListDiffEntry[] listDiffEntries = new ListDiffEntry[oldValues
							.size()];
					int[] positions = (int[]) msg.getNewValue();
					if (positions == null) {
						int index = 0;
						for (Object oldValue : oldValues) {
							listDiffEntries[index] = Diffs.createListDiffEntry(
									0, false, oldValue);
							++index;
						}
					} else {
						int index = 0;
						for (Object oldValue : oldValues) {
							listDiffEntries[index] = Diffs.createListDiffEntry(
									positions[index] - index, false, oldValue);
							++index;
						}
					}
					diff = Diffs.createListDiff(listDiffEntries);
					break;
				}
				case Notification.SET:
				case Notification.RESOLVE: {
					ListDiffEntry[] listDiffEntries = new ListDiffEntry[2];
					int pos = msg.getPosition();
					// Looks like a single valued feature
					if (pos == -1) {
						pos = 0;
					}
					listDiffEntries[0] = Diffs.createListDiffEntry(pos, false, msg.getOldValue());
					listDiffEntries[1] = Diffs.createListDiffEntry(pos, true, msg.getNewValue());
					diff = Diffs.createListDiff(listDiffEntries);
					break;
				}
				case Notification.MOVE: {
					Object movedValue = msg.getNewValue();
					ListDiffEntry[] listDiffEntries = new ListDiffEntry[2];
					listDiffEntries[0] = Diffs.createListDiffEntry(
							(Integer) msg.getOldValue(), false, movedValue);
					listDiffEntries[1] = Diffs.createListDiffEntry(
							msg.getPosition(), true, movedValue);
					diff = Diffs.createListDiff(listDiffEntries);
					break;
				}
				case Notification.UNSET: {
					// This just represents going back to the unset state, but
					// that doesn't affect the contents of the list.
					//
					return;
				}
				default: {
					throw new UnsupportedOperationException(msg.toString());
				}
				}
				getListener().handleEvent(
						(new SimplePropertyEvent(SimplePropertyEvent.CHANGE,
								msg.getNotifier(), getOwner(), diff)));
			}
		}
	}

}
