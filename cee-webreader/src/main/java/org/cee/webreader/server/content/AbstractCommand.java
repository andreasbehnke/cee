package org.cee.webreader.server.content;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCommand implements Command {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);
	
	private static final String ERROR_RUNNING_COMMAND = "Error running command";

	private List<CommandCallback> callbacks = new ArrayList<CommandCallback>();
	
	protected abstract void runInternal();
	
	private void runWithErrorNotification() {
		try {
			runInternal();
		} catch (Exception ex) {
			LOG.error(ERROR_RUNNING_COMMAND, ex);
			fireError(ex);
		} finally {
			fireFinished();
		}
	}
	
	@Override
	public void run() {
		runWithErrorNotification();
	}
	
	@Override
	public void addCommandCallback(CommandCallback callback) {
		callbacks.add(callback);
	}

	protected void fireFinished() {
		for (CommandCallback callback : callbacks) {
			callback.notifyFinished();
		}
	}
	
	protected void fireError(Exception ex) {
		for (CommandCallback callback : callbacks) {
			callback.notifyError(ex);
		}
	}
}
