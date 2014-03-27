package untitled6.client.gui.creation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class JndiInputPanel extends Composite implements Creator<JndiItemPair> {

    private final TextBox propValueTB;

    private final TextBox propNameTB;

    public JndiInputPanel(){
        HorizontalPanel panel = new HorizontalPanel();
        this.propNameTB = new TextBox();
        this.propValueTB = new TextBox();
        panel.add(propNameTB);
        panel.add(propValueTB);
        initWidget(panel);
    }

    @Override
    public JndiItemPair create() {
        String propName= propNameTB.getText();
        String propValue = propValueTB.getText();
        return new JndiItemPair(propName,propValue);
    }
}
