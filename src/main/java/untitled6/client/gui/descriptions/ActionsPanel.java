package untitled6.client.gui.descriptions;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.FullServiceDTO;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.ResponseDTO;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionRegistrationDTO;
import com.icl.integrator.dto.registration.AddActionDTO;
import untitled6.client.GenericCallback;
import untitled6.client.GreetingServiceAsync;
import untitled6.client.gui.CreationListener;
import untitled6.client.gui.creation.dialog.AddActionDialog;
import untitled6.client.gui.creation.dialog.EditActionDialog;

import java.util.List;

public class ActionsPanel extends Composite {

    private final GreetingServiceAsync service = GreetingServiceAsync.Util.getInstance();

    private final ListBox listBox;

    private final VerticalPanel infoPanel;

    private List<ActionEndpointDTO<ActionDescriptor>> actions;

    private FullServiceDTO<ActionDescriptor> fullService;

    public ActionsPanel() {
        HorizontalPanel mainPanel = new HorizontalPanel();
        VerticalPanel leftPanel = new VerticalPanel();
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        Button addServiceButton = new Button("+");
        addServiceButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new AddActionDialog(fullService.getEndpoint().getEndpointType(),
                    new CreationListener<ActionRegistrationDTO<ActionDescriptor>>() {
                        @Override
                        public void onCreated(
                                ActionRegistrationDTO<ActionDescriptor> value) {
                            final ServiceDTO serviceDTO = new ServiceDTO(fullService.getServiceName(),
                                                                   fullService.getEndpoint()
                                                                           .getEndpointType());
                            AddActionDTO<ActionDescriptor> addActionDTO =
                                    new AddActionDTO<>(serviceDTO, value);
                            service.addAction(new IntegratorPacket<>(addActionDTO),
                                              new GenericCallback<ResponseDTO<Void>>("ADDACTION") {
                                @Override
                                public void onSuccess(ResponseDTO<Void> result) {
                                    PopupPanel widgets = new PopupPanel(true,false);
                                    widgets.setWidget(new Label("OKAYADD"));
                                    widgets.center();
                                    service.getSupportedActions(
                                            new IntegratorPacket<>(serviceDTO),
                                            new GenericCallback<ResponseDTO<List<
                                                    ActionEndpointDTO<ActionDescriptor>>>>("getsuppactions") {
                                        @Override
                                        public void onSuccess(
                                                ResponseDTO<List<ActionEndpointDTO<ActionDescriptor>>> result) {
                                                    setAllActions(result.getResponse().getResponseValue());
                                        }
                                    });
                                }
                            });
                        }
                    }).center();
            }
        });
        Button removeAction = new Button("-");
        removeAction.setEnabled(false);
        Button editActionButton = new Button("edit action");
        editActionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new EditActionDialog(fullService.getEndpoint().getEndpointType(),
                 new CreationListener<ActionEndpointDTO<ActionDescriptor>>() {
                    @Override
                    public void onCreated(ActionEndpointDTO<ActionDescriptor> value) {

                    }
                });
            }
        });
        buttonsPanel.add(addServiceButton);
        buttonsPanel.add(removeAction);
        leftPanel.add(buttonsPanel);
        listBox = new ListBox();
        listBox.setVisibleItemCount(10);
        listBox.addClickHandler(new CH());
        leftPanel.add(listBox);
        mainPanel.add(leftPanel);
        infoPanel = new VerticalPanel();
        mainPanel.add(infoPanel);
        mainPanel.setBorderWidth(1);
        mainPanel.setSpacing(2);
        initWidget(mainPanel);
    }

    public void setActions(FullServiceDTO<ActionDescriptor> service) {
        this.fullService = service;
//        this.titleLabel.setText("Actions of " + service.getServiceName());
        setAllActions(service.getActions());
    }

    private void setAllActions(List<ActionEndpointDTO<ActionDescriptor>> actions) {
        this.actions = actions;
        listBox.clear();
        for (ActionEndpointDTO item : actions) {
            listBox.addItem(item.getActionName());
        }
        if (!actions.isEmpty()) {
            listBox.setSelectedIndex(0);
            fillViews(0);
        } else {
            infoPanel.clear();
        }
    }

    private void fillViews(int index) {
        infoPanel.clear();
        infoPanel.add(new ActionDescriptorPanel(actions.get(index)));
    }


    public ActionEndpointDTO<ActionDescriptor> getSelected(){
        int selectedIndex = listBox.getSelectedIndex();
        return actions.get(selectedIndex);
    }

    private class CH implements ClickHandler {

        @Override
        public void onClick(ClickEvent clickEvent) {
            int selectedIndex = listBox.getSelectedIndex();
            fillViews(selectedIndex);
        }
    }
}
