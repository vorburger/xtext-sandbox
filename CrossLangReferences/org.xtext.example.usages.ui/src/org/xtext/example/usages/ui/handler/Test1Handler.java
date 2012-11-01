package org.xtext.example.usages.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.xtext.example.repository.ModelRepository;
import org.xtext.example.usages.usages.Model;

import com.google.inject.Inject;

public class Test1Handler extends AbstractHandler implements IHandler {
	
	@Inject
	IResourceSetProvider resourceSetProvider;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof IFile) {
				IFile file = (IFile) firstElement;
				IProject project = file.getProject();

				URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
				
				Model model1 = getModelViaResourceSetProviderInjectedIntoThisPlugin(project, uri);
				String name1 = model1.getUsages().get(0).getDefinition().getName();

				Model model2 = (Model) ModelRepository.getModel(project, uri);
				String name2 = model2.getUsages().get(0).getDefinition().getName();
				
				IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
				MessageDialog.openInformation(window.getShell(), "Test", 
						"Name from ModelViaResourceSetProviderInjectedIntoThisPlugin: " + name1 + "\n"
						+ "Name from ModelRespository: " + name2);
			}
		}
		
		return null;
	}

	private Model getModelViaResourceSetProviderInjectedIntoThisPlugin(IProject project, URI uri) {
		ResourceSet rs = resourceSetProvider.get(project);
		Resource r = rs.getResource(uri, true);
		Model m = (Model) r.getContents().get(0);
		return m;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
