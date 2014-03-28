package untitled6.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import untitled6.client.Images;

public final class AppLoadingView extends PopupPanel {

    private final Images images = GWT.create(Images.class);

    public AppLoadingView() {
        Image widget = new Image(images.loading());
        widget.setWidth("75px");
        widget.setHeight("75px");
        add(widget);
        setModal(true);
    }
}