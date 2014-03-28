package untitled6.client.gui.creation.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.util.EndpointType;
import untitled6.client.gui.CreationListener;
import untitled6.client.gui.descriptions.HttpActionInputDescriptionPanel;
import untitled6.client.gui.descriptions.JmsActionInputDescriptorPanel;
import untitled6.client.util.Creator;

/**
 * Created by e.shahmaev on 25.03.2014.
 */
public class EditActionDialog extends DialogBox {

    private Creator<ActionEndpointDTO<ActionDescriptor>> actionDescriptorCreator;

    public EditActionDialog(EndpointType endpointType, final
    CreationListener<ActionEndpointDTO<ActionDescriptor>>
            creationListener) {
        DockPanel dock = new DockPanel();
        dock.setSpacing(4);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button createButton = new Button("save", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                creationListener.onCreated(actionDescriptorCreator.create());
            }
        });
        Button closeButton = new Button("Close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        buttonPanel.add(closeButton);
        buttonPanel.add(createButton);
        dock.add(buttonPanel, DockPanel.SOUTH);
        VerticalPanel mainPanel = new VerticalPanel();
        if (endpointType == EndpointType.HTTP) {
            actionDescriptorCreator = new HttpActionInputDescriptionPanel();
        } else {
            actionDescriptorCreator = new JmsActionInputDescriptorPanel();
        }
        mainPanel.add((Composite) actionDescriptorCreator);
        dock.add(mainPanel, DockPanel.CENTER);
        setWidget(dock);
    }
}
