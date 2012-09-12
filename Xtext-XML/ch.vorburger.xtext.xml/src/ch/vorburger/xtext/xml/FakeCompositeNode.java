/*******************************************************************************
 * Copyright (c) 2012 Michael Vorburger (http://www.vorburger.ch).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package ch.vorburger.xtext.xml;

import java.util.NoSuchElementException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.nodemodel.BidiIterable;
import org.eclipse.xtext.nodemodel.BidiTreeIterable;
import org.eclipse.xtext.nodemodel.BidiTreeIterator;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.SyntaxErrorMessage;

/**
 * ICompositeNode (INode) used by NameURISwapperImpl,
 * for name:/ URI cross references which point to EObjects
 * which don't exist yet.
 *  
 * @author Michael Vorburger
 */
public class FakeCompositeNode implements ICompositeNode, Adapter {

	protected static final BidiTreeIterator<INode> emptyBidiTreeIterator = new BidiTreeIterator<INode>() {
		@Override
		public boolean hasPrevious() {
			return false;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean hasNext() {
			return false;
		}
		
		@Override
		public void prune() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public INode previous() {
			throw new NoSuchElementException();
		}
		
		@Override
		public INode next() {
			throw new NoSuchElementException();
		}
	};

	private static final BidiTreeIterable<INode> emptyIterable = new BidiTreeIterable<INode>() {
		@Override
		public BidiTreeIterable<INode> reverse() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public BidiTreeIterator<INode> iterator() {
			return emptyBidiTreeIterator;
		}
	};
	
	private final String text;
	private EObject semanticElement;

	public FakeCompositeNode(String text) {
		super();
		this.text = text;
	}

	// ---------------------------------------------------
	// ICompositeNode methods

	@Override
	public ICompositeNode getParent() {
		return null;
	}

	@Override
	public boolean hasSiblings() {
		throw new UnsupportedOperationException();
		// return false;
	}

	@Override
	public boolean hasPreviousSibling() {
		throw new UnsupportedOperationException();
		// return false;
	}

	@Override
	public boolean hasNextSibling() {
		throw new UnsupportedOperationException();
		// return false;
	}

	@Override
	public INode getPreviousSibling() {
		throw new UnsupportedOperationException();
		// return null;
	}

	@Override
	public INode getNextSibling() {
		return null;
	}

	@Override
	public ICompositeNode getRootNode() {
		throw new UnsupportedOperationException();
		// return null;
	}

	@Override
	public Iterable<ILeafNode> getLeafNodes() {
		throw new UnsupportedOperationException();
		// return null;
	}

	@Override
	public int getTotalOffset() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getOffset() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getTotalLength() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getLength() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getTotalEndOffset() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getTotalStartLine() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getStartLine() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getTotalEndLine() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public int getEndLine() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public EObject getGrammarElement() {
		// This is one hell of an ugly hack...
		// ... we can't return null here as it would cause NPEs
		// so returning ANY EObject (say EClass) which isn't Grammar 
		// related so that this doesn't match. 
		return EcorePackage.eINSTANCE.eClass();
	}

	@Override
	public EObject getSemanticElement() {
		throw new UnsupportedOperationException();
		// return null;
	}

	@Override
	public boolean hasDirectSemanticElement() {
		throw new UnsupportedOperationException();
		// return false;
	}

	@Override
	public SyntaxErrorMessage getSyntaxErrorMessage() {
		throw new UnsupportedOperationException();
		// return null;
	}

	@Override
	public BidiTreeIterable<INode> getAsTreeIterable() {
		return emptyIterable;
	}

	@Override
	public BidiIterable<INode> getChildren() {
		throw new UnsupportedOperationException();
		// return null;
	}

	@Override
	public boolean hasChildren() {
		throw new UnsupportedOperationException();
		// return false;
	}

	@Override
	public INode getFirstChild() {
		return null;
	}

	@Override
	public INode getLastChild() {
		return null;
	}

	@Override
	public int getLookAhead() {
		throw new UnsupportedOperationException();
		// return 0;
	}

	// ---------------------------------------------------
	// Adapter methods
	// These are shamelessly copy/pasted from org.eclipse.xtext.nodemodel.impl.CompositeNodeWithSemanticElement
	
	@Override
	public Notifier getTarget() {
		return semanticElement;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type instanceof Class<?> && INode.class.isAssignableFrom((Class<?>)type);
	}

	@Override
	public void notifyChanged(Notification msg) {
		// ignore
	}

	@Override
	public void setTarget(Notifier newTarget) {
		if (newTarget == null || newTarget instanceof EObject)
			semanticElement = (EObject) newTarget;
		else
			throw new IllegalArgumentException("Notifier must be an Eobject");
	}

}
