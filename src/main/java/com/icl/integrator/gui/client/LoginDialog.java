package com.icl.integrator.gui.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.client.gxt.MainContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * Created by BigBlackBug on 26.05.2014.
 */
public class LoginDialog extends AbstractIntegratorDialog {

	private static final String DEFAULT_LOGIN = "user";

	private static final String DEFAULT_PASSWORD = "pass";

	private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

	private final TextField loginTF;

	private final TextField passTF;

	public LoginDialog() {
		super("Логин");
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		loginTF = new TextField();
		loginTF.setText(DEFAULT_LOGIN);
		passTF = new TextField();
		passTF.setText(DEFAULT_PASSWORD);
		hlc.add(createLabel("Логин", loginTF),
		        new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1,
		                                                           DEFAULT_MARGINS)
		);
		hlc.add(createLabel("Пароль", passTF),
		        new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1,
		                                                           DEFAULT_MARGINS)
		);
		dialog.setWidget(hlc);
		dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		TextButton changeServer = dialog.getButton(PredefinedButton.OK);
		changeServer.setText("Другой сервер");
		changeServer.addSelectHandler(new SelectEvent.SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				dialog.hide();
				new PingDialog().show();
			}
		});
		final TextButton loginButton = dialog.getButton(PredefinedButton.CANCEL);
		loginButton.setText("Залогиниться");
		loginButton.addSelectHandler(new SelectEvent.SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				dialog.hide();
				service.login(loginTF.getText(), passTF.getText(), new GenericCallback<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Info.display("залогинились", "ура");
						RootPanel.get().add(new MainContentPanel());
					}

					@Override
					public void onFailure(Throwable caught) {
						dialog.show();
					}
				});
			}
		});
	}

}
