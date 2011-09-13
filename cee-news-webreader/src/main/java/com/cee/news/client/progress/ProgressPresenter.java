package com.cee.news.client.progress;

public class ProgressPresenter {

	public ProgressPresenter(final ProgressModel model, final ProgressView view) {
		model.addProgressHandler(new ProgressHandler() {
			
			@Override
			public void onProgress(int percentComplete) {
				if (percentComplete == 100) {
					view.setVisible(false);
				} else {
					view.setVisible(true);
					view.setPercentReady(percentComplete);
				}
			}
		});
	}
}
