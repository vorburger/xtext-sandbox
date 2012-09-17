package ch.vorburger.xtext.databinding;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.concurrent.IWriteAccess;

public interface IXtextValueProperty extends IEMFValueProperty {

	// TODO Change argument type from IWriteAccess<XtextResource> to IXtextResourceReadWriteAccess once XTextDocument implements that
	IObservableValue observe(IWriteAccess<XtextResource> source);

	// TODO Change argument type from IWriteAccess<XtextResource> to IXtextResourceReadWriteAccess once XTextDocument implements that
	IObservableValue observe(Realm realm, IWriteAccess<XtextResource> source);

//	public IObservableValue observe(EObject source);

	IXtextValueProperty value(EStructuralFeature feature);
	
}
