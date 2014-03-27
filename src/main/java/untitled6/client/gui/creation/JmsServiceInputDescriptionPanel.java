package untitled6.client.gui.creation;

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
public class JmsServiceInputDescriptionPanel extends Composite
        implements Creator<EndpointDescriptor> {

    private final TextBox cfTB;

    private final HorizontalPanel mainPanel;

    private final VerticalPanel jndiPanel;

    private List<JndiInputPanel> jndiPanels = new ArrayList<>();

    public JmsServiceInputDescriptionPanel() {
        mainPanel = new HorizontalPanel();
        jndiPanel = new VerticalPanel();
        mainPanel.add(new HTML("<br><b>connectionfactory:</b>"));
        cfTB = new TextBox();
        mainPanel.add(cfTB);
        mainPanel.add(new HTML("<br><b>JNDI:</b>"));
        jndiPanel.add(new JndiInputPanel());
        mainPanel.add(jndiPanel);
        mainPanel.add(new Button("addprop",new Handler()));
        initWidget(mainPanel);
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
            JndiInputPanel newPanel = new JndiInputPanel();
            jndiPanel.add(newPanel);
            jndiPanels.add(newPanel);
        }
    }
}
