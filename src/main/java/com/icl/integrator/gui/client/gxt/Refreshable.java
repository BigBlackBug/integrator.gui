package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Created by BigBlackBug on 26.05.2014.
 */
public interface Refreshable<T> extends IsWidget {

	public void refresh(T item);

}
