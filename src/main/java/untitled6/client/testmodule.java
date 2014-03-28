package untitled6.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.ResponseDTO;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import untitled6.client.gui.creation.dialog.DeliveryButtonClickHandler;
import untitled6.client.gui.descriptions.ActionsPanel;
import untitled6.client.gui.descriptions.ServicesPanel;

import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class testmodule implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
//    private final GreetingServiceAsync service = GreetingServiceAsync.Util.getInstance();

    private final GreetingServiceAsync service = GreetingServiceAsync.Util.getInstance();
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Подрубиться");

		final TextBox nameField = new TextBox();
		final TextBox portField = new TextBox();
		nameField.setText("localhost");
        portField.setText("8080");
		final Label errorLabel = new Label();
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("hostContainer").add(nameField);
		RootPanel.get("portContainer").add(portField);
		RootPanel.get("initClientContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		sendButton.setFocus(true);
//		nameField.selectAll();

		// Create the popup dialog box
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// First, we validate the input.
				errorLabel.setText("");
				final String textToServer = nameField.getText();
				// Then, we send the input to the server.
                textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				String text = portField.getText();
				int i = Integer.parseInt(text);
				GenericCallback<Void> callback = new GenericCallback<Void>("INIT") {

					@Override
					public void onSuccess(Void result) {
                        service.getServiceList(
								new IntegratorPacket<Void, DestinationDescriptor>(),
								new GenericCallback<ResponseDTO<List<ServiceDTO>>>("GSL") {
									@Override
									public void onSuccess(ResponseDTO<List<ServiceDTO>> result) {
										List<ServiceDTO> services =
												result.getResponse().getResponseValue();
                                        ActionsPanel actionsPanel = new ActionsPanel();
                                        RootPanel.get("actionsContainer").add(actionsPanel);
                                        RootPanel.get("servicesContainer").add(
                                                new ServicesPanel(services,actionsPanel));
                                        RootPanel.get("deliveryButton").add(
                                                new Button("Доставка",
                                                           new DeliveryButtonClickHandler()));
									}
								});
					}
				};
                service.initClient(nameField.getText(), "", i, callback);
			}
		});
	}
}
