/*******************************************************************************
 * Copyright (c) 2023 Bachmann electronic GmbH and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * 
 * Gesa Hentschke (Bachmann electronic GmbH) - initial implementation
 *******************************************************************************/

package org.eclipse.cdt.lsp.server;

import java.util.function.Supplier;

import org.eclipse.cdt.lsp.LspPlugin;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;

public final class EnableExpression {
	private final Expression expression;
	private final Supplier<IEvaluationContext> parent;

	public EnableExpression(Supplier<IEvaluationContext> parent, Expression expression) {
		this.expression = expression;
		this.parent = parent;
	}

	/**
	 * Evaluates enable expression with the given context. The context defualtVariable can be set by the input document.
	 *
	 * @return true if expression evaluates to true, false otherwise
	 */
	public boolean evaluate(Object document) {
		try {
			if (document == null) {
				document = new Object();
			}
			final var context = new EvaluationContext(parent.get(), document);
			context.setAllowPluginActivation(true);
			return expression.evaluate(context).equals(EvaluationResult.TRUE);
		} catch (CoreException e) {
			LspPlugin.logError("Error occured during evaluation of enablement expression", e); //$NON-NLS-1$
		}
		return false;
	}

}
