package org.eclipse.cdt.lsp;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.UIJob;

public class LspUtils {
	
	/**
	 * Checks if given ContentType id matches the content types for C/C++ files.
	 * 
	 * @param id ContentType id
	 * @return {@code true} if C/C++ content type
	 */
	public static boolean isCContentType(String id) {
		// TODO: The content type definition from TM4E "lng.cpp" can be omitted if either https://github.com/eclipse-cdt/cdt/pull/310 or 
		// https://github.com/eclipse/tm4e/pull/500 has been merged.
		return ( id.startsWith("org.eclipse.cdt.core.c") && (id.endsWith("Source") || id.endsWith("Header")) ) || "lng.cpp".equals(id);
	}
	
	/**
	 * Show error dialog to user
	 * @param title
	 * @param errorText
	 * @param status
	 */
	public static void showErrorMessage(final String title, final String errorText, final Status status) {
		UIJob job = new UIJob("LSP Utils") //$NON-NLS-1$
		{
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				ErrorDialog.openError(getActiveShell(), title, errorText, status);
				return Status.OK_STATUS;
			}
		};
		job.setSystem(true);
		job.schedule();
	}
	
	private static Shell getActiveShell() {
		if (PlatformUI.getWorkbench() != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null)
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		return null;
	}
	
	public static boolean isFileOpenedInLspEditor(URI uri) {
		if (uri == null) {
			return false;
		}
		var editors = getEditors();
		if (!editors.isEmpty()) {
			for (IEditorReference editor : editors) {
				IEditorInput editorInput = null;
				URI editorUnputURI = null;
				try {
					editorInput = editor.getEditorInput();
				} catch (PartInitException e) {
					LspPlugin.logError(e.getMessage(), e);
					continue;
				}
				
				if (editorInput instanceof IURIEditorInput) {
					editorUnputURI = ((IURIEditorInput) editorInput).getURI();
				} else if (editorInput instanceof FileEditorInput) {
					editorUnputURI = ((FileEditorInput) editorInput).getFile().getLocationURI();
				}

				if (uri.equals(editorUnputURI)) {
					// should return false when an external header file with same URI is opened in a LSP editor 
					// and non LSP editor and tab switching from a non LSP editor to the tab with the file in the non LSP editor:
					return LspPlugin.LSP_C_EDITOR_ID.equals(editor.getEditor(true).getEditorSite().getId()) && isLspEditorActive();
				}
			}
			// the file has not been opened yet -> goto definition/declaration case
			return isLspEditorActive();
		}
		return false;
	}
	
	public static boolean isFileOpenedInLspEditor(IEditorInput editorInput) {
		if (editorInput == null) {
			return false;
		}
		var editors = getEditors();
		if (!editors.isEmpty()) {
			for (IEditorReference editor : editors) {
				IEditorInput editorInputFromEditor = null;
				try {
					editorInputFromEditor = editor.getEditorInput();
				} catch (PartInitException e) {
					LspPlugin.logError(e.getMessage(), e);
					continue;
				}
				if (editorInput.equals(editorInputFromEditor)) {
					return LspPlugin.LSP_C_EDITOR_ID.equals(editor.getEditor(true).getEditorSite().getId());
				}
			}
			// the file has not been opened yet:
			return isLspEditorActive();
		}
		return false;
	}
	
	public static List<IEditorReference> getEditors() {
		List<IEditorReference> editorsList = new ArrayList<>();
		for (var window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (var page : window.getPages()) {
				for (var editor : page.getEditorReferences()) {
					editorsList.add(editor);
				}
			}
		}
		return editorsList;
	}
	
	private static boolean isLspEditorActive() {
		var activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null && activeWorkbenchWindow.getActivePage() != null) {
			var activeEditor = activeWorkbenchWindow.getActivePage().getActiveEditor();
			if (activeEditor != null) {
				return LspPlugin.LSP_C_EDITOR_ID.equals(activeEditor.getEditorSite().getId());
			}
		}
		return false;
	}

}