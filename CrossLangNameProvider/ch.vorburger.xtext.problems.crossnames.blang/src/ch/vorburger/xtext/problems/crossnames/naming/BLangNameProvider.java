package ch.vorburger.xtext.problems.crossnames.naming;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import ch.vorburger.xtext.problems.crossnames.aLang.ModelA;
import ch.vorburger.xtext.problems.crossnames.bLang.BLangPackage;
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

//      Do *NOT* resolve cross-references in order to create EObjectDescriptions! Else you'll hit a "Cyclic resolution of lazy links" AssertionError...
//      @see http://www.eclipse.org/forums/index.php/m/707310/
//      @see http://www.eclipse.org/forums/index.php/m/734839/
//      @see http://www.eclipse.org/forums/index.php/m/802015/
//      @see http://www.eclipse.org/forums/index.php/m/730141/
//
//        ModelA forA = b.getModelA();
//        if (forA == null)
//            return null;
//        final String aName = forA.getName();
//        
//      Instead like this:
        List<INode> nodes = NodeModelUtils.findNodesForFeature(b, BLangPackage.Literals.MODEL_B__MODEL_A);
        INode node = nodes.get(0);
        final String aName = NodeModelUtils.getTokenText(node);
        
        return QualifiedName.create(aName, b.getLocalNonUniqueName());
    }
}
