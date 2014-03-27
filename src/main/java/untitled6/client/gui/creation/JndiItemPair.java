package untitled6.client.gui.creation;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class JndiItemPair {

    private final String propName;

    private final String propValue;

    public JndiItemPair(String propName, String propValue) {
        this.propName = propName;
        this.propValue = propValue;
    }

    public String getPropName() {
        return propName;
    }

    public String getPropValue() {
        return propValue;
    }
}
