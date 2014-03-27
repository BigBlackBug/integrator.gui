package untitled6.client.gui.creation.dialog;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import untitled6.client.gui.creation.Creator;

/**
 * Created by e.shahmaev on 25.03.2014.
 */
public abstract class AbstractActionInputDescriptionPanel extends Composite implements
        Creator<ActionEndpointDTO<ActionDescriptor>> {

    protected final VerticalPanel mainPanel;

    protected final TextBox actionNameTB;

    protected AbstractActionInputDescriptionPanel() {
        mainPanel = new VerticalPanel();
        mainPanel.add(new HTML("<b>actoinName</b>"));
        actionNameTB = new TextBox();
        mainPanel.add(actionNameTB);
        initWidget(mainPanel);
    }

    protected AbstractActionInputDescriptionPanel(ActionEndpointDTO actionEndpointDTO){
        this();
        actionNameTB.setText(actionEndpointDTO.getActionName());
    }

    @Override
    public ActionEndpointDTO<ActionDescriptor> create() {
        ActionDescriptor actionDescriptor = createActionDescriptor();
        return new ActionEndpointDTO<>(actionNameTB.getText(),actionDescriptor);
    }

    protected abstract ActionDescriptor createActionDescriptor();

}
