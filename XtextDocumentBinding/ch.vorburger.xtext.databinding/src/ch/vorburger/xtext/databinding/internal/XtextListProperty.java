package ch.vorburger.xtext.databinding.internal;

import java.util.List;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.masterdetail.MasterDetailObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.emf.databinding.internal.EMFListProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import ch.vorburger.xtext.databinding.internal.sourceadapt.SourceAccessor;
import ch.vorburger.xtext.databinding.internal.sourceadapt.XTextDocumentSourceAccessor;

@SuppressWarnings("restriction")
public class XtextListProperty extends EMFListProperty {

	public XtextListProperty(EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
	}

	@Override
	public INativePropertyListener adaptListener(final ISimplePropertyListener listener) {
		return new XtextPropertyListener.XtextListPropertyListener() {
		@Override
	        protected IProperty getOwner() {
	          return XtextListProperty.this;
	        }

	        @Override
	        protected ISimplePropertyListener getListener() {
	          return listener;
	        }

			@Override
	        protected EStructuralFeature getFeature() {
	          return XtextListProperty.this.getFeature();
	        }
	      };
	  }

	@Override
	protected List<?> doGetList(Object source) {
		SourceAccessor sourceAccessor = (SourceAccessor) source;
		Object obj = sourceAccessor.eGet(getFeature());
		if (obj instanceof List<?>) {
			return (List<?>) obj;
		}
		return super.doGetList(source);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void doSetList(Object source, List list, ListDiff diff) {
		List< ? > currentList = doGetList(source);
	    diff.applyTo(currentList);
	}

	@Override
	public IObservableList observeDetail(IObservableValue master) {
		return MasterDetailObservables.detailList(master, listFactory(master), getElementType());
	}

	protected IObservableFactory listFactory(final IObservableValue master) {
		return new IObservableFactory() {
			public IObservable createObservable(Object target) {
				return observe(master.getRealm(), getSourceAccessorWrapper(master, target));
			}
		};
	}

	protected Object getSourceAccessorWrapper(IObservableValue master, Object target) {
		if (master instanceof IObserving) {
			IObserving observing = (IObserving) master;
			Object observed = observing.getObserved();
			if (observed instanceof XTextDocumentSourceAccessor) {
				XTextDocumentSourceAccessor masterAccessor = (XTextDocumentSourceAccessor) observed;
				EObject eObject = (EObject) target;
				return new XTextDocumentSourceAccessor(masterAccessor, eObject);
			} else
				throw new IllegalArgumentException("IObservableList master is an IObserving, but not an XTextDocumentSourceAccessor: " + master);
		} else
			throw new IllegalArgumentException("IObservableList master is not an IObserving: " + master);
	}

}
