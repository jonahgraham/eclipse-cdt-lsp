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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SwitchWorkspaceToLspPage extends WizardPage {
	private Button changeWorkspaceSetting;

	public SwitchWorkspaceToLspPage() {
		super("SwitchWorkspaceToLspPage"); //$NON-NLS-1$
		setTitle("Switch Workspace to use LSP");
		setDescription("Current workspace will have the default changed to use the C/C++ Editor (LSP).");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 5;
		container.setLayout(layout);

		changeWorkspaceSetting = new Button(container, SWT.CHECK);
		changeWorkspaceSetting.setText("&Change workspace default to use C/C++ Editor (LSP)");
		changeWorkspaceSetting.setSelection(true);
		changeWorkspaceSetting.addListener(SWT.Selection, (e) -> {
		});
		GridDataFactory.fillDefaults().span(2, 1).applyTo(changeWorkspaceSetting);

		setControl(container);
		Dialog.applyDialogFont(container);
		// TODO add help
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(container, IHelpContextIds.UPDATE_CLASSPATH);
	}

	public void storeSettings() {
	}

	public boolean changeWorkspaceSetting() {
		return changeWorkspaceSetting.getSelection();
	}

	@Override
	public boolean isPageComplete() {
		return true;
	}
}
