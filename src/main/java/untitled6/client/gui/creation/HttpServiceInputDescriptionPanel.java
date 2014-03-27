package untitled6.client.gui.creation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.source.HttpEndpointDescriptorDTO;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class HttpServiceInputDescriptionPanel extends Composite implements
        Creator<EndpointDescriptor> {

    private final TextBox hostTB;

    private final TextBox portTB;

    public HttpServiceInputDescriptionPanel() {
        HorizontalPanel panel = new HorizontalPanel();

        panel.add(new HTML("<br><b>Host:</b>"));
        hostTB = new TextBox();
        panel.add(hostTB);
        panel.add(new HTML("<br><b>Port:</b>"));
        portTB = new TextBox();
        panel.add(portTB);
        initWidget(panel);
    }

    @Override
    public EndpointDescriptor create() {
        String host = hostTB.getText();
        String port = portTB.getText();
        return new HttpEndpointDescriptorDTO(host, Integer.parseInt(port));
    }
}
