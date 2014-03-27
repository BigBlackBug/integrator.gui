package untitled6.client.gui.creation;

import com.google.gwt.user.client.ui.ListBox;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class EnumListBox<T extends Enum<T>> extends ListBox implements Creator<T> {

    private final Class<T> enumType;

    private final List<T> values;

    @SafeVarargs
    public EnumListBox(Class<T> enumType,T... exclusions) {
        super();
        this.enumType = enumType;
        EnumSet<T>exc = EnumSet.noneOf(enumType);
        for(T exclusion:exclusions){
            exc.add(exclusion);
        }
        this.values = new ArrayList<>();
        for (T item : enumType.getEnumConstants()) {
            if(!exc.contains(item)){
                values.add(item);
                addItem(item.toString());
            }
        }
        setSelectedIndex(0);
    }

    @Override
    public T create() {
        return values.get(getSelectedIndex());
    }

    public void select(T value) {
        setSelectedIndex(values.indexOf(value));
    }
}