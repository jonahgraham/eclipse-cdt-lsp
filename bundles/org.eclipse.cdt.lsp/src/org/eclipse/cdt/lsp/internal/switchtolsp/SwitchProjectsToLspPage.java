/*******************************************************************************
 *  Copyright (c) 2000, 2024 IBM Corporation and others.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.cdt.lsp.internal.switchtolsp;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class SwitchProjectsToLspPage extends WizardPage {
	private final IProject[] selectedProjects;
	private final IProject[] projects;
	private CheckboxTableViewer projectListViewer;
	private final TablePart tablePart;

	public class ProjectsContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object parent) {
			if (projects != null)
				return projects;
			return new Object[0];
		}
	}

	private class TablePart extends WizardCheckboxTablePart {
		public TablePart(String mainLabel) {
			super(mainLabel);
		}

		@Override
		public void updateCounter(int count) {
			super.updateCounter(count);
			dialogChanged();
		}
	}

	public SwitchProjectsToLspPage(IProject[] models, IProject[] selected) {
		super("SwitchProjectsToLspPage"); //$NON-NLS-1$
		setTitle("Switch Project to use LSP");
		setDescription("Selected projects will have their default changed to use the C/C++ Editor (LSP).");
		this.projects = models;
		this.selectedProjects = selected;
		tablePart = new TablePart("&Available C/C++ Projects:");
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 5;
		container.setLayout(layout);

		tablePart.createControl(container);

		projectListViewer = tablePart.getTableViewer();
		projectListViewer.setContentProvider(new ProjectsContentProvider());
		projectListViewer.setLabelProvider(new WorkbenchLabelProvider());

		GridData gd = (GridData) tablePart.getControl().getLayoutData();
		gd.heightHint = 300;
		gd.widthHint = 300;

		projectListViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		tablePart.setSelection(selectedProjects);

		setControl(container);
		Dialog.applyDialogFont(container);
		// TODO add help
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(container, IHelpContextIds.UPDATE_CLASSPATH);
	}

	public void storeSettings() {
	}

	public Object[] getSelected() {
		return tablePart.getSelection();
	}

	private void dialogChanged() {
		setPageComplete(tablePart.getSelectionCount() > 0);
	}

	@Override
	public boolean isPageComplete() {
		return tablePart.getSelectionCount() > 0;
	}
}
