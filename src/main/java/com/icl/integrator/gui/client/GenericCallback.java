package com.icl.integrator.gui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by BigBlackBug on 3/17/14.
 */
public abstract class GenericCallback<T> implements AsyncCallback<T> {

//	private final DialogBox dialogBox;
//
//	private final String message;
//
//    private HTML serverMessageLabel;
//
//    protected GenericCallback(String message) {
//		this.message = message;
//		this.dialogBox = createDialogBox();
//	}
//
//	private DialogBox createDialogBox() {
//		final DialogBox dialogBox = new DialogBox();
//		dialogBox.setText("Remote Procedure Call");
//		dialogBox.setAnimationEnabled(true);
//		final Button closeButton = new Button("Close");
//		closeButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				dialogBox.hide();
//			}
//		});
//		closeButton.getElement().setId("closeButton");
//		VerticalPanel dialogVPanel = new VerticalPanel();
//		dialogVPanel.addStyleName("dialogVPanel");
//		dialogVPanel.add(new HTML("<b>Message:</b>"));
//		final HTML serverResponseLabel = new HTML();
//		serverResponseLabel.addStyleName("serverResponseLabelError");
//		serverResponseLabel.setHTML(message);
//		dialogVPanel.add(serverResponseLabel);
//        serverMessageLabel = new HTML();
//        serverMessageLabel.setHTML("");
//        dialogVPanel.add(serverMessageLabel);
//		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//		dialogVPanel.add(closeButton);
//		dialogBox.setWidget(dialogVPanel);
//		dialogBox.setText("Remote Procedure Call Failure");
//		return dialogBox;
//	}

	@Override
	public void onFailure(Throwable caught) {
//        Throwable cause = caught.getCause();
//        serverMessageLabel.setHTML(cause != null ? cause.toString() : caught.toString());
//        dialogBox.center();
	}
}
