package com.icl.integrator.gui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.icl.integrator.dto.ErrorDTO;

/**
 * Created by BigBlackBug on 3/17/14.
 */
public abstract class GenericCallback<T> implements AsyncCallback<T> {

    public void onError(ErrorDTO errorDTO){

    }

	@Override
	public void onFailure(Throwable caught) {
	}
}
