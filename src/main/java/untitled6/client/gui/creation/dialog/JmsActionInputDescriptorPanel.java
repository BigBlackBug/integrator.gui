package untitled6.client.gui.creation.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionMethod;
import com.icl.integrator.dto.registration.QueueDTO;
import untitled6.client.gui.EnumListBox;

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
        password = new TextBox();
        password.setWidth("100%");
        queueName = new TextBox();
        queueName.setWidth("100%");
        username = new TextBox();
        username.setWidth("100%");
        table.setWidget(1, 0, new HTML("<b>Тип действия:</b>"));
        table.setWidget(1, 1, actionMethodLB);

        table.setWidget(2, 0, new HTML("<b>Название очереди:</b>"));
        table.setWidget(2, 1, queueName);

        table.setWidget(3, 0, new HTML("<b>Имя пользователя:</b>"));
        table.setWidget(3, 1, username);

        table.setWidget(4, 0, new HTML("<b>Пароль:</b>"));
        table.setWidget(4, 1, password);

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
