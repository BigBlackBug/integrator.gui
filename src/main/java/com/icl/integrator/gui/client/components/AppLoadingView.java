package com.icl.integrator.gui.client.components;

import com.google.gwt.core.client.GWT;
import com.icl.integrator.gui.client.Images;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;

public final class AppLoadingView {

	private final Images images = GWT.create(Images.class);

	private final AutoProgressMessageBox box;

	public AppLoadingView() {
		box = new AutoProgressMessageBox("Пингуем");
		box.setProgressText("Посылаем запрос к серверу...");
	}

	public void show() {
		box.auto();
		box.show();
	}

	public void hide() {
		box.hide();
	}
}