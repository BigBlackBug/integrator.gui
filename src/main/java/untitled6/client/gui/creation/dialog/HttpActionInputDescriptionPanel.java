package untitled6.client.gui.creation.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionMethod;
import com.icl.integrator.dto.registration.HttpActionDTO;
import untitled6.client.gui.creation.EnumListBox;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class HttpActionInputDescriptionPanel extends AbstractActionInputDescriptionPanel{

    private final TextBox pathTB;

    private final EnumListBox<ActionMethod> actionMethodLB;

    public HttpActionInputDescriptionPanel() {
        super();
        actionMethodLB = new EnumListBox<>(ActionMethod.class);
        mainPanel.add(actionMethodLB);
        mainPanel.add(new HTML("<br><b>path:</b>"));
        pathTB = new TextBox();
        mainPanel.add(pathTB);
    }

    public HttpActionInputDescriptionPanel(ActionEndpointDTO<HttpActionDTO> actionEndpointDTO){
        this();
        HttpActionDTO actionDescriptor = actionEndpointDTO.getActionDescriptor();
        pathTB.setText(actionDescriptor.getPath());
        actionMethodLB.select(actionDescriptor.getActionMethod());
    }

    @Override
    protected ActionDescriptor createActionDescriptor() {
        return new HttpActionDTO(pathTB.getText(), actionMethodLB.create());
    }

}
