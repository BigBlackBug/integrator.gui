package com.icl.integrator.gui.client;

import com.google.gwt.user.client.ui.InlineLabel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Created by BigBlackBug on 26.05.2014.
 */
public abstract class AbstractIntegratorDialog extends Dialog {

	protected static final Margins DEFAULT_MARGINS = new Margins(4, 4, 4, 4);

	protected final Dialog dialog;

	protected AbstractIntegratorDialog(String heading) {
		dialog = new Dialog();
		dialog.setResizable(false);
		dialog.setWidth(400);
		dialog.setHeight(130);
		dialog.setBlinkModal(true);
		dialog.setModal(true);
		dialog.setHeadingText(heading);
	}

	protected VerticalLayoutContainer createLabel(String text, TextField tf) {
		VerticalLayoutContainer vc = new VerticalLayoutContainer();
		vc.add(new InlineLabel(text),
		       new VerticalLayoutContainer.VerticalLayoutData(1, 0.5, DEFAULT_MARGINS));
		vc.add(tf,
		       new VerticalLayoutContainer.VerticalLayoutData(1, 0.5, DEFAULT_MARGINS));
		return vc;
	}

	public void show() {
		dialog.show();

	}
}
