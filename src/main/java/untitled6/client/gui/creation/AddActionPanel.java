package untitled6.client.gui.creation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionRegistrationDTO;
import com.icl.integrator.dto.util.EndpointType;
import untitled6.client.gui.CreationListener;
import untitled6.client.gui.descriptions.HttpActionInputDescriptionPanel;
import untitled6.client.gui.descriptions.JmsActionInputDescriptorPanel;
import untitled6.client.util.Creator;

/**
 * Created by e.shahmaev on 20.03.14.
 */
public class AddActionPanel extends Composite{

    private final CheckBox forceRegisterCB;

    private Creator<ActionEndpointDTO<ActionDescriptor> > actionDescriptorCreator;

    public AddActionPanel(EndpointType endpointType, final
    CreationListener<ActionRegistrationDTO<ActionDescriptor>> creationListener) {
        DockPanel dock = new DockPanel();
        dock.setSpacing(4);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(3);
        Button createButton = new Button("Добавить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ActionEndpointDTO<ActionDescriptor> action = actionDescriptorCreator.create();
                boolean forceRegister = forceRegisterCB.getValue();
                creationListener.onCreated(new ActionRegistrationDTO<>(action, forceRegister));
            }
        });
        Button closeButton = new Button("Не добавить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                creationListener.onCreated(null);
            }
        });
        buttonPanel.add(closeButton);
        buttonPanel.add(createButton);
        dock.add(buttonPanel, DockPanel.SOUTH);
        if (endpointType == EndpointType.HTTP) {
            actionDescriptorCreator = new HttpActionInputDescriptionPanel();
        } else {
            actionDescriptorCreator = new JmsActionInputDescriptorPanel();
        }
        forceRegisterCB = new CheckBox("Регать несмотря ни на что?");
        FlexTable table = new FlexTable();
        table.setWidget(0, 0, forceRegisterCB);
        table.setWidget(1, 0, (Composite) actionDescriptorCreator);
        dock.add(table, DockPanel.CENTER);
        setWidget(dock);
    }

}
