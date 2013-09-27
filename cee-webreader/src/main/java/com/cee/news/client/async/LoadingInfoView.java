package com.cee.news.client.async;

/**
 * Displays loading information and animation
 */
public interface LoadingInfoView {

	public void showLoading(String message);
	
	public void hideLoading();
}
