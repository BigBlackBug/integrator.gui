package untitled6.client.gui.creation.dialog;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import untitled6.client.util.Creator;

/**
 * Created by e.shahmaev on 25.03.2014.
 */
public abstract class AbstractActionInputDescriptionPanel extends Composite implements
        Creator<ActionEndpointDTO<ActionDescriptor>> {

    protected final FlexTable table;

    protected final TextBox actionNameTB;

    protected AbstractActionInputDescriptionPanel() {
        table = new FlexTable();
        table.setCellSpacing(3);
        HTML widget = new HTML("<b>Название действия:</b>");
        widget.setWordWrap(false);
        table.setWidget(0, 0, widget);
        actionNameTB = new TextBox();
        actionNameTB.setWidth("100%");
        table.setWidget(0, 1, actionNameTB);
        initWidget(table);
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
