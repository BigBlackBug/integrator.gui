package com.icl.integrator.gui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.client.components.display.ActionsPanel;
import com.icl.integrator.gui.client.components.display.ServicesPanel;
import com.icl.integrator.gui.shared.FieldVerifier;
import com.icl.integrator.gui.shared.GuiException;

import java.util.List;

public class testmodule implements EntryPoint {

    private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

		public void onModuleLoad() {
		final Button sendButton = new Button("Подрубиться");

		final TextBox nameField = new TextBox();
		final TextBox portField = new TextBox();
        final TextBox deployPathField = new TextBox();
		nameField.setText("192.168.83.91");
        portField.setText("18080");
        deployPathField.setText("integrator");
		final Label errorLabel = new Label();
		sendButton.addStyleName("sendButton");

		RootPanel.get("hostContainer").add(nameField);
		RootPanel.get("portContainer").add(portField);
        RootPanel.get("deployPathContainer").add(deployPathField);
		RootPanel.get("initClientContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		sendButton.setFocus(true);
		final Button closeButton = new Button("Close");
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				final String textToServer = nameField.getText();
                textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				String portString = portField.getText();
                int port;
                try {
                    port = FieldVerifier.parseNumber(portString, 1, 65535);
                }catch(GuiException ex){
                    PopupPanel widgets = new PopupPanel(true,false);
                    widgets.setWidget(new Label(ex.getMessage()));
                    widgets.center();
                    return;
                }
				GenericCallback<Void> callback = new GenericCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
                        service.getServiceList(
								new IntegratorPacket<Void, DestinationDescriptor>(),
								new GenericCallback<List<ServiceDTO>>() {
									@Override
									public void onSuccess(List<ServiceDTO> result) {
                                        ActionsPanel actionsPanel = new ActionsPanel();
                                        RootPanel.get("actionsContainer").add(actionsPanel);
                                        RootPanel.get("servicesContainer").add(
                                                new ServicesPanel(result,actionsPanel));
                                        RootPanel.get("deliveryButton").add(
                                                new Button("Доставка",
                                                           new DeliveryButtonClickHandler()));
									}
								});
					}
				};
                service.initClient(nameField.getText(), deployPathField.getText(), port, callback);
			}
		});
	}
}
