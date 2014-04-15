package com.icl.integrator.gui.client.components.creation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.source.JMSEndpointDescriptorDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class JmsServiceInputDescriptionPanel extends InputDescriptionPanel {

    private final FlexTable table = new FlexTable();

    private final TextBox cfTB;

    private List<JndiInputPanel> jndiPanels = new ArrayList<>();

    public JmsServiceInputDescriptionPanel() {

        cfTB = new TextBox();
        cfTB.setWidth("100%");
        table.setWidget(0, 0, new HTML("<b>ConnectionFactory:</b>"));
        table.setWidget(0, 1, cfTB);
        table.setWidget(1, 0, new Button("Добавить настройку JNDI", new Handler()));
        table.getFlexCellFormatter().setColSpan(1, 0, 2);
        table.setCellSpacing(2);
        initWidget(table);
        setWidth("100%");
    }

    @Override
    public FlexTable getTable() {
        return table;
    }

    @Override
    public EndpointDescriptor create() {
        String connectionFactory = cfTB.getText();
        Map<String, String> jndiMAP = new HashMap<>();
        for (JndiInputPanel panel : jndiPanels) {
            JndiItemPair jndiItemPair = panel.create();
            jndiMAP.put(jndiItemPair.getPropName(),jndiItemPair.getPropValue());
        }
        return new JMSEndpointDescriptorDTO(connectionFactory,jndiMAP);
    }

    private class Handler implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            JndiInputPanel newPanel = new JndiInputPanel(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                table.removeRow(table.getRowCount()-1);
                jndiPanels.remove(jndiPanels.size()-1);
                }
            });
            table.setWidget(3 + jndiPanels.size(), 0, newPanel);
            table.getFlexCellFormatter().setColSpan(3 + jndiPanels.size(), 0, 2);
            jndiPanels.add(newPanel);
        }
    }
}
