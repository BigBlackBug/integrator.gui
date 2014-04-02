package com.icl.integrator.gui.client.components.creation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.gui.client.components.display.ActionDescriptorPanel;

import java.util.List;

/**
 * Created by e.shahmaev on 21.03.14.
 */
public class ActionDisplayPanel extends Composite {

    private final ListBox listBox;

    private final VerticalPanel infoPanel;

    private List<ActionEndpointDTO<ActionDescriptor>> actions;

    private String serviceName;

    public ActionDisplayPanel() {
        HorizontalPanel mainPanel = new HorizontalPanel();
        VerticalPanel leftPanel = new VerticalPanel();
        listBox = new ListBox();
        listBox.setVisibleItemCount(10);
        listBox.addClickHandler(new CH());
        leftPanel.add(listBox);
        mainPanel.add(leftPanel);
        infoPanel = new VerticalPanel();
        mainPanel.add(infoPanel);
        initWidget(mainPanel);
    }

    public void setActions(String service, List<ActionEndpointDTO<ActionDescriptor>> actions) {
        this.serviceName = service;
        setAllActions(actions);
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
        infoPanel.add(new ActionDescriptorPanel(actions.get(index), serviceName));
    }


    public ActionEndpointDTO<ActionDescriptor> getSelected() {
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
