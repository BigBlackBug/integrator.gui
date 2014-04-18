package com.icl.integrator.gui.client.components;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by BigBlackBug on 18.04.2014.
 */
public class FixedBorderTextBox extends SimplePanel {

	private TextBox textBox;

	public FixedBorderTextBox() {
		textBox = new TextBox();
		textBox.addStyleName("wide");
		setStyleName("textbox");
		add(textBox);
	}

	public String getText() {
		return textBox.getText();
	}

	public TextBox getTextBox() {
		return textBox;
	}

	public void setText(String text) {
		textBox.setText(text);
	}
}
