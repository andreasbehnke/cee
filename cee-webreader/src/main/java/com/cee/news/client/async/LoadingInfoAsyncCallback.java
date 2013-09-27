package com.cee.news.client.async;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoadingInfoAsyncCallback {

	private final LoadingInfoView loadingInfoView;

	public LoadingInfoAsyncCallback(final LoadingInfoView loadingInfoView) {
	    this.loadingInfoView = loadingInfoView;
    }
	
	public <T> AsyncCallback<T> call(final String loadingMessage, final AsyncCallback<T> target) {
		loadingInfoView.showLoading(loadingMessage);
		return new AsyncCallback<T>() {
			@Override
			public void onSuccess(T result) {
			    try {
			    	target.onSuccess(result);
			    } finally {
			    	loadingInfoView.hideLoading();
			    }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				try {
			    	target.onFailure(caught);
			    } finally {
			    	loadingInfoView.hideLoading();
			    }
			}
		};
	}
}
