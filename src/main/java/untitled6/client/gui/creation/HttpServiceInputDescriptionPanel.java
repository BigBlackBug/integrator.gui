package untitled6.client.gui.creation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.source.HttpEndpointDescriptorDTO;
import untitled6.client.util.Creator;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class HttpServiceInputDescriptionPanel extends Composite implements
        Creator<EndpointDescriptor> {

    private final TextBox hostTB;

    private final TextBox portTB;

    public HttpServiceInputDescriptionPanel() {
        FlexTable table = new FlexTable();
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
    public EndpointDescriptor create() {
        String host = hostTB.getText();
        String port = portTB.getText();  //TODO validate
        return new HttpEndpointDescriptorDTO(host, Integer.parseInt(port));
    }
}
