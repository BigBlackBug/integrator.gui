package untitled6.client.gui.creation.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionMethod;
import com.icl.integrator.dto.registration.QueueDTO;
import untitled6.client.gui.creation.EnumListBox;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class JmsActionInputDescriptorPanel extends AbstractActionInputDescriptionPanel{

    private final TextBox queueName;

    private final TextBox username;

    private final TextBox password;

    private final EnumListBox<ActionMethod> actionMethodLB;

    public JmsActionInputDescriptorPanel() {
        actionMethodLB = new EnumListBox<>(ActionMethod.class);
        mainPanel.add(actionMethodLB);
        mainPanel.add(new HTML("<br><b>queuname:</b>"));
        queueName = new TextBox();
        mainPanel.add(queueName);
        mainPanel.add(new HTML("<br><b>username:</b>"));
        username = new TextBox();
        mainPanel.add(username);
        mainPanel.add(new HTML("<br><b>pass:</b>"));
        password = new TextBox();
        mainPanel.add(password);
    }

    public JmsActionInputDescriptorPanel(ActionEndpointDTO<QueueDTO> actionEndpointDTO){
        this();
        QueueDTO actionDescriptor = actionEndpointDTO.getActionDescriptor();
        queueName.setText(actionDescriptor.getQueueName());
        username.setText(actionDescriptor.getUsername());
        password.setText(actionDescriptor.getPassword());
    }

    @Override
    protected ActionDescriptor createActionDescriptor() {
        return new QueueDTO(queueName.getText(),username.getText(),password.getText(),
                            actionMethodLB.create());
    }
}
