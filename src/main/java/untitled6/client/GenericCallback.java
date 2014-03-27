package untitled6.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by BigBlackBug on 3/17/14.
 */
public abstract class GenericCallback<T> implements AsyncCallback<T> {

	private final DialogBox dialogBox;

	private final String message;

    private HTML serverMessageLabel;

    protected GenericCallback(String message) {
		this.message = message;
		this.dialogBox = createDialogBox();
	}

	private DialogBox createDialogBox() {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<br><b>Message:</b>"));
		final HTML serverResponseLabel = new HTML();
		serverResponseLabel.addStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(message);
		dialogVPanel.add(serverResponseLabel);
        serverMessageLabel = new HTML();
        serverMessageLabel.setHTML("");
        dialogVPanel.add(serverMessageLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setText("Remote Procedure Call Failure");
		return dialogBox;
	}

	@Override
	public void onFailure(Throwable caught) {
        serverMessageLabel.setHTML(caught.getCause().toString());
		dialogBox.center();
	}
}
