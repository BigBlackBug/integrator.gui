package com.icl.integrator.gui.client;

import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.shared.FieldVerifier;
import com.icl.integrator.gui.shared.GuiException;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

public final class PingDialog extends AbstractIntegratorDialog {

	private static final String DEFAULT_HOST = "localhost";

	private static final String DEFAULT_PORT = "8081";

	private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

	private final TextField hostTF;

	private final TextField pathTF;

	private final TextField portTF;

	public PingDialog() {
		super("Доступность сервера");
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		hostTF = new TextField();
		hostTF.setText(DEFAULT_HOST);
		portTF = new TextField();
		portTF.setText(DEFAULT_PORT);
		pathTF = new TextField();
		hlc.add(createLabel("Хост", hostTF),
		        new HorizontalLayoutContainer.HorizontalLayoutData(0.3, 1,
		                                                           DEFAULT_MARGINS)
		);
		hlc.add(createLabel("Порт", portTF),
		        new HorizontalLayoutContainer.HorizontalLayoutData(0.3, 1,
		                                                           DEFAULT_MARGINS)
		);
		hlc.add(createLabel("Путь", pathTF),
		        new HorizontalLayoutContainer.HorizontalLayoutData(0.4, 1,
		                                                           DEFAULT_MARGINS)
		);
		dialog.setWidget(hlc);
		final TextButton ok = dialog.getButtonById("OK");
		ok.setText("Пингануть");
		ok.addSelectHandler(new SelectEvent.SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String portString = portTF.getText();
                int port;
                try {
                    port = FieldVerifier.parseNumber(portString, 1, 65535);
                }catch(GuiException ex){
	                Info.display("Херовый порт", ex.getMessage());
                    return;
                }
				dialog.hide();
				service.initClient(hostTF.getText(),pathTF.getText(),port,new GenericCallback<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
					new LoginDialog().show();
					}

					@Override
					public void onFailure(Throwable caught) {
						dialog.show();
					}
				});
			}
		});
//		ok.addSelectHandler(new SelectEvent.SelectHandler() {
//			@Override
//			public void onSelect(SelectEvent event) {
//				dialog.hide();
//			}
//		});
	}

}