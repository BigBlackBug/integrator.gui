package untitled6.client.gui.creation.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionRegistrationDTO;
import com.icl.integrator.dto.util.EndpointType;
import untitled6.client.gui.CreationListener;
import untitled6.client.gui.creation.Creator;

/**
 * Created by e.shahmaev on 20.03.14.
 */
public class AddActionPanel extends Composite{

    private final CheckBox forceReigsterCB;

    private Creator<ActionEndpointDTO<ActionDescriptor> > actionDescriptorCreator;

    public AddActionPanel(EndpointType endpointType, final
    CreationListener<ActionRegistrationDTO<ActionDescriptor>>
            creationListener) {
        DockPanel dock = new DockPanel();
        dock.setSpacing(4);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button createButton = new Button("Create", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ActionEndpointDTO<ActionDescriptor> action = actionDescriptorCreator.create();
                boolean forceRegister = forceReigsterCB.getValue();
                creationListener.onCreated(new ActionRegistrationDTO<>(action, forceRegister));
            }
        });
        Button closeButton = new Button("Close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                creationListener.onCreated(null);
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
        HorizontalPanel checkBOxPanle = new HorizontalPanel();
        checkBOxPanle.add(new HTML("forceRegsiter"));
        forceReigsterCB = new CheckBox();
        checkBOxPanle.add(forceReigsterCB);
        mainPanel.add(checkBOxPanle);
        mainPanel.add((Composite) actionDescriptorCreator);
        dock.add(mainPanel, DockPanel.CENTER);
        setWidget(dock);
    }

}
