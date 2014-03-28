package untitled6.client.gui.creation.dialog;

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

//TODO засунуть сюда addActionpanel
/**
 * Created by e.shahmaev on 19.03.14.
 */
public class AddActionDialog extends DialogBox {

    private final CheckBox forceRegisterCB;

    private Creator<ActionEndpointDTO<ActionDescriptor>> actionDescriptorCreator;

    public AddActionDialog(EndpointType endpointType, final
    CreationListener<ActionRegistrationDTO<ActionDescriptor>>
            creationListener) {
        setText("Создание Действия");
        DockPanel dock = new DockPanel();
        dock.setSpacing(4);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(3);
        Button createButton = new Button("Создать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                ActionEndpointDTO<ActionDescriptor> action = actionDescriptorCreator.create();
                boolean forceRegister = forceRegisterCB.getValue();
                creationListener.onCreated(new ActionRegistrationDTO<>(action, forceRegister));
            }
        });
        Button closeButton = new Button("Не создать", new ClickHandler() {
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
        forceRegisterCB = new CheckBox("Регать несмотря ни на что?");
        FlexTable table = new FlexTable();
        table.setWidget(0, 0, forceRegisterCB);
        table.setWidget(1, 0, (Composite) actionDescriptorCreator);
        dock.add(table, DockPanel.CENTER);
        setWidget(dock);
    }

}
