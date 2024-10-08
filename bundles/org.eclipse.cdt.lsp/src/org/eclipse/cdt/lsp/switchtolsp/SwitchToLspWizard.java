/*******************************************************************************
 * Copyright (c) 2024 Kichwa Coders Canada Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.cdt.lsp.switchtolsp;

import java.util.Arrays;

import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.lsp.internal.switchtolsp.SwitchProjectsToLsp;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.ILog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class SwitchToLspWizard {

	public SwitchToLspWizard() {
	}

	public void doSwitch() {
		doSwitch(new IProject[0]);
	}

	public void doSwitch(IProject initialSelection) {
		if (initialSelection == null) {
			doSwitch(new IProject[0]);
		}
		doSwitch(new IProject[] { initialSelection });
	}

	public void doSwitch(IProject[] initialSelection) {
		IProject[] projects = getCProjects();

		SwitchProjectsToLsp wizard = new SwitchProjectsToLsp(projects, initialSelection);
		final WizardDialog dialog = new WizardDialog(getActiveWorkbenchShell(), wizard);
		BusyIndicator.showWhile(getActiveWorkbenchShell().getDisplay(), () -> dialog.open());
	}

	private static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	private static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}

	private IProject[] getCProjects() {

		ICModel model = CoreModel.getDefault().getCModel();
		IProject[] projects;
		try {
			ICProject[] cprojects;
			cprojects = model.getCProjects();
			projects = Arrays.stream(cprojects).map((p) -> p.getProject()).toArray(IProject[]::new);
		} catch (CModelException e) {
			ILog.of(getClass()).error("Failed to identify C projects in workspace", e); //$NON-NLS-1$
			projects = new IProject[0];
		}
		return projects;

	}

}
