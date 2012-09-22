package ch.vorburger.xtext.databinding.internal;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.emf.databinding.internal.EMFListPropertyDecorator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

import ch.vorburger.xtext.databinding.IXtextListProperty;
import ch.vorburger.xtext.databinding.IXtextValueProperty;
import ch.vorburger.xtext.databinding.internal.sourceadapt.SourceAccessor;
import ch.vorburger.xtext.databinding.internal.sourceadapt.XTextDocumentSourceAccessor;
import ch.vorburger.xtext.databinding.internal.sourceadapt.XtextResourceDelegatingAccess;

@SuppressWarnings("restriction")
public class XtextListPropertyDecorator extends EMFListPropertyDecorator implements IXtextListProperty {

	public XtextListPropertyDecorator(IListProperty delegate, EStructuralFeature eStructuralFeature) {
		super(delegate, eStructuralFeature);
	}

	@Override
	@SuppressWarnings("unchecked")
	public IObservableList observe(Object source) {
		if (source instanceof SourceAccessor) {
			return observe((SourceAccessor) source);
		} else if (source instanceof IWriteAccess<?>) {
			return observe((IWriteAccess<XtextResource>) source);
		} else {
			throw new IllegalArgumentException("source object to observe is not an IWriteAccess<XtextResource> (nor a SourceAccessor already): " + source);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public IObservableList observe(Realm realm, Object source) {
		if (source instanceof SourceAccessor) {
			return observe(realm, (SourceAccessor) source);
		} else if (source instanceof IWriteAccess<?>) {
			return observe(realm, (IWriteAccess<XtextResource>) source);
		} else {
			throw new IllegalArgumentException("source object to observe is not an IWriteAccess<XtextResource> (nor a SourceAccessor already): " + source);
		}
	}

	@Override
	public IObservableList observe(IWriteAccess<XtextResource> source) {
		// // TODO Remove gimmick once XTextDocument implements IXtextResourceReadWriteAccess
		XtextResourceDelegatingAccess gimmick = new XtextResourceDelegatingAccess(source);
		SourceAccessor wrappedSource = new XTextDocumentSourceAccessor(gimmick);
		return observe(wrappedSource);
	}

	@Override
	public IObservableList observe(Realm realm,	IWriteAccess<XtextResource> source) {
		// TODO Remove gimmick once XTextDocument implements IXtextResourceReadWriteAccess
		XtextResourceDelegatingAccess gimmick = new XtextResourceDelegatingAccess(source);
		SourceAccessor wrappedSource = new XTextDocumentSourceAccessor(gimmick);
		return observe(realm, wrappedSource);
	}

	@Override
	public IXtextListProperty values(IXtextValueProperty property) {
		return new XtextListPropertyDecorator(super.values(property), property.getStructuralFeature());
	}


}
