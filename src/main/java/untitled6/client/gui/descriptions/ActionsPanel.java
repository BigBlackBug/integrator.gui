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

import java.util.List;

public class ActionsPanel extends Composite {

    private final GreetingServiceAsync service = GreetingServiceAsync.Util.getInstance();

    private final ListBox listBox;

    private final VerticalPanel infoPanel;

    private List<ActionEndpointDTO<ActionDescriptor>> actions;

    private FullServiceDTO<ActionDescriptor> fullService;

    public ActionsPanel() {
        Button addActionButton = new Button("+");
        addActionButton.setWidth("100%");
        addActionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new AddActionDialog(fullService.getEndpoint().getEndpointType(),
                                    new CreationListener<ActionRegistrationDTO<ActionDescriptor>>() {
                                        @Override
                                        public void onCreated(
                                                ActionRegistrationDTO<ActionDescriptor> value) {
                                            final ServiceDTO serviceDTO =
                                                    new ServiceDTO(fullService.getServiceName(),
                                                                   fullService.getEndpoint()
                                                                           .getEndpointType()
                                                    );
                                            AddActionDTO<ActionDescriptor> addActionDTO =
                                                    new AddActionDTO<>(serviceDTO, value);
                                            service.addAction(new IntegratorPacket<>(addActionDTO),
                                                              new GenericCallback<ResponseDTO<Void>>(
                                                                      "ADDACTION") {
                                                                  @Override
                                                                  public void onSuccess(
                                                                          ResponseDTO<Void> result) {
                                                                      PopupPanel widgets =
                                                                              new PopupPanel(true,
                                                                                             false);
                                                                      widgets.setWidget(
                                                                              new Label("OKAYADD"));
                                                                      widgets.center();
                                                                      service.getSupportedActions(
                                                                              new IntegratorPacket<>(
                                                                                      serviceDTO),
                                                                              new GenericCallback<ResponseDTO<List<
                                                                                      ActionEndpointDTO<ActionDescriptor>>>>(
                                                                                      "getsuppactions") {
                                                                                  @Override
                                                                                  public void onSuccess(
                                                                                          ResponseDTO<List<ActionEndpointDTO<ActionDescriptor>>> result) {
                                                                                      setAllActions(
                                                                                              result.getResponse()
                                                                                                      .getResponseValue()
                                                                                                   );
                                                                                  }
                                                                              }
                                                                                                 );
                                                                  }
                                                              }
                                                             );
                                        }
                                    }
                ).center();
            }
        });
        Button removeAction = new Button("-");
        removeAction.setWidth("100%");
        removeAction.setEnabled(false);
        listBox = new ListBox();
        listBox.setVisibleItemCount(10);
        listBox.addClickHandler(new ActionsListBoxClickHandler());
        infoPanel = new VerticalPanel();

        FlexTable table = new FlexTable();
        table.setWidget(0, 0, addActionButton);
        table.setWidget(0, 1, removeAction);
        table.setWidget(1, 0, listBox);
        table.getFlexCellFormatter().setColSpan(1, 0, 2);
        table.setWidget(0, 2, infoPanel);
        table.getFlexCellFormatter().setRowSpan(0, 2, 3);
        table.setBorderWidth(1);
        initWidget(table);
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

    private class ActionsListBoxClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent clickEvent) {
            int selectedIndex = listBox.getSelectedIndex();
            fillViews(selectedIndex);
        }
    }
}
