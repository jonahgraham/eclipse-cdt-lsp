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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.FrameworkUtil;

public class SwitchProjectsToLsp extends Wizard {
	private final IProject[] selectedProjects;
	private final IProject[] projects;
	private SwitchWorkspaceToLspPage workspacePage;
	private SwitchProjectsToLspPage projectsPage;
	private static final String STORE_SECTION = "SwitchProjectsToLsp"; //$NON-NLS-1$

	public SwitchProjectsToLsp(IProject[] projects, IProject[] selectedProjects) {
		IDialogSettings bundleSettings = getBundleDialogSettings();
		setDialogSettings(getSettingsSection(bundleSettings));
		// TODO add image
		//		setDefaultPageImageDescriptor(PDEPluginImages.DESC_CONVJPPRJ_WIZ);
		setWindowTitle("Switch to C/C++ Editor (LSP)");
		setNeedsProgressMonitor(true);
		this.selectedProjects = selectedProjects;
		this.projects = projects;
	}

	private IDialogSettings getBundleDialogSettings() {
		var bundle = FrameworkUtil.getBundle(getClass());

		return PlatformUI.getDialogSettingsProvider(bundle).getDialogSettings();
	}

	private IDialogSettings getSettingsSection(IDialogSettings main) {
		IDialogSettings setting = main.getSection(STORE_SECTION);
		if (setting == null) {
			setting = main.addNewSection(STORE_SECTION);
		}
		return setting;
	}

	@Override
	public boolean performFinish() {
		if (!PlatformUI.getWorkbench().saveAllEditors(true)) {
			return false;
		}
		Object[] finalSelected = projectsPage.getSelected();
		projectsPage.storeSettings();
		List<IProject> projectsToSwitch = Arrays.stream(finalSelected).map(IProject.class::cast).toList();
		// TODO do the work
		//UpdateClasspathJob.scheduleFor(modelArray, true);
		return true;
	}

	@Override
	public void addPages() {
		workspacePage = new SwitchWorkspaceToLspPage();
		addPage(workspacePage);
		projectsPage = new SwitchProjectsToLspPage(projects, selectedProjects);
		addPage(projectsPage);
	}
}
