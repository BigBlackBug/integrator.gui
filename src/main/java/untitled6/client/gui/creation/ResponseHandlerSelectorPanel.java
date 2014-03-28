package untitled6.client.gui.creation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.icl.integrator.dto.ServiceAndActions;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import untitled6.client.util.Creator;

import java.util.Map;

/**
 * Created by e.shahmaev on 21.03.14.
 */
public class ResponseHandlerSelectorPanel extends Composite
        implements Creator<DestinationDescriptor> {

    private final ListBox servicesLB;

    private final ActionDisplayPanel actionsPanel;

    private Map<String, ServiceAndActions<ActionDescriptor>> services;

    public ResponseHandlerSelectorPanel(Map<String,ServiceAndActions<ActionDescriptor>> serviceList) {
        HorizontalPanel panel = new HorizontalPanel();
        this.services = serviceList;
        servicesLB = new ListBox();
        servicesLB.setVisibleItemCount(10);
        servicesLB.setMultipleSelect(false);
        servicesLB.setVisibleItemCount(10);
        servicesLB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                refillViews(servicesLB.getValue(servicesLB.getSelectedIndex()));
            }
        });
        this.actionsPanel = new ActionDisplayPanel();
        panel.add(servicesLB);
        panel.add(actionsPanel);
        refresh(serviceList);
        initWidget(panel);
    }

    private void refresh(Map<String, ServiceAndActions<ActionDescriptor>> services) {
        this.services = services;
        servicesLB.clear();
        for (Map.Entry<String, ServiceAndActions<ActionDescriptor>> service : services.entrySet()) {
            String serviceName = service.getKey();
            servicesLB.addItem(serviceName);
        }
        if (!services.isEmpty()) {
            servicesLB.setSelectedIndex(0);
            String value = servicesLB.getValue(0);
            refillViews(value);
        }
    }

    private void refillViews(String serviceName) {
        actionsPanel.setActions(serviceName,services.get(serviceName).getActions());
    }

    @Override
    public DestinationDescriptor create() {
        int selectedIndex = servicesLB.getSelectedIndex();
        String serviceName = servicesLB.getValue(selectedIndex);

        ActionEndpointDTO<ActionDescriptor> endpointDTO = actionsPanel.getSelected();
        return new ServiceDestinationDescriptor(serviceName, endpointDTO.getActionName(),
                                                endpointDTO.getActionDescriptor()
                                                        .getEndpointType());
    }
}
