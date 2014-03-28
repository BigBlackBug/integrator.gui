package untitled6.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import untitled6.client.Images;

public final class AppLoadingView extends PopupPanel {

    private final Images images = GWT.create(Images.class);

    public AppLoadingView() {
        Image widget = new Image(images.loading());
        widget.setWidth("100px");
        widget.setHeight("100px");
        add(widget);
        setModal(true);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void stopProcessing() {
        hide();
    }

    public void startProcessing() {
        center();
        show();
    }
}