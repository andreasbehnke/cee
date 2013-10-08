package org.cee.webreader.client.async;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
