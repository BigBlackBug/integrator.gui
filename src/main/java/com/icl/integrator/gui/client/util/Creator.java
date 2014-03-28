package com.icl.integrator.gui.client.util;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public interface Creator<T> {

    public T create() throws CreationException;
}
