package com.icl.integrator.gui.client.gxt;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * Created by BigBlackBug on 26.05.2014.
 */
public abstract class ImmutableValueProvider<T,V> implements ValueProvider<T,V> {

	@Override
	public void setValue(T object, V value) {

	}

	@Override
	public String getPath() {
		return "";
	}
}
