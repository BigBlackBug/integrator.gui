package untitled6.client.gui.creation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icl.integrator.dto.registration.DeliverySettingsDTO;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class DeliverySettingsPanel extends Composite implements Creator<DeliverySettingsDTO> {

    public DeliverySettingsPanel(){
        initWidget(new VerticalPanel());
    }
    @Override
    public DeliverySettingsDTO create() {
        //TODO implement
        return new DeliverySettingsDTO(5,5000);
    }
}
