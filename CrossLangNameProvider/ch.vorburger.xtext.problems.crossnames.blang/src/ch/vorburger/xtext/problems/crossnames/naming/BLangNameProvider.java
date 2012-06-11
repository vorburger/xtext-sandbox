package ch.vorburger.xtext.problems.crossnames.naming;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import ch.vorburger.xtext.problems.crossnames.aLang.ModelA;
import ch.vorburger.xtext.problems.crossnames.bLang.ModelB;

/**
 * IQualifiedNameProvider for B.
 * 
 * @author Michael Vorburger
 */
public class BLangNameProvider extends IQualifiedNameProvider.AbstractImpl {

    @Override
    public QualifiedName getFullyQualifiedName(EObject obj) {
        if (obj == null)
            return null;
        ModelB b = (ModelB) obj;
        ModelA forA = b.getModelA();
        if (forA == null)
            return null;
        return QualifiedName.create(forA.getName(), b.getLocalNonUniqueName());
    }
}
