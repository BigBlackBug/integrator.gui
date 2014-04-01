package com.icl.integrator.gui.client.components.creation;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.source.HttpEndpointDescriptorDTO;
import com.icl.integrator.gui.client.util.CreationException;
import com.icl.integrator.gui.shared.FieldVerifier;
import com.icl.integrator.gui.shared.GuiException;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class HttpServiceInputDescriptionPanel extends InputDescriptionPanel {

    private final TextBox hostTB;

    private final TextBox portTB;

    private FlexTable table = new FlexTable();

    public HttpServiceInputDescriptionPanel() {
        hostTB = new TextBox();
        hostTB.setWidth("100%");
        portTB = new TextBox();
        portTB.setWidth("100%");
        table.setWidget(0, 0, new HTML("<b>Хост:</b>"));
        table.setWidget(0, 1, hostTB);
        table.setWidget(1, 0, new HTML("<b>Порт:</b>"));
        table.setWidget(1, 1, portTB);

        initWidget(table);
        setWidth("100%");
    }

    @Override
    public EndpointDescriptor create() throws CreationException {
        String host = hostTB.getText();
        String port = portTB.getText();
        try {
            FieldVerifier.validateIP(host);
        } catch (GuiException gex) {
            throw new CreationException(gex, "Адрес сервиса");
        }
        try {
            int portNumber = FieldVerifier.parseNumber(port, 1);
            return new HttpEndpointDescriptorDTO(host, portNumber);
        } catch (GuiException gex) {
            throw new CreationException(gex, "Порт сервиса");
        }
    }

    @Override
    public FlexTable getTable() {
        return table;
    }
}
