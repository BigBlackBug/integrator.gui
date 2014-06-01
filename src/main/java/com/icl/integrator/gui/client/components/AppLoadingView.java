package com.icl.integrator.gui.client.components;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;

//TODO proper duck typing mask/unmask
public final class AppLoadingView extends Component {

	private final AutoProgressMessageBox box;

	public AppLoadingView() {
		box = new AutoProgressMessageBox("Пингуем");
		box.setProgressText("Посылаем запрос к серверу...");
	}

	@Override
	public void mask() {
		box.auto();
		box.show();
	}

	@Override
	public void mask(String message) {
		box.setHeadingHtml(message);
		mask();
	}

	@Override
	public void unmask() {
		box.hide();
	}
}