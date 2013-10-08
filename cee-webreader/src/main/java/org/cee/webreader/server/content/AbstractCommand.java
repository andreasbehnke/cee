package org.cee.webreader.server.content;

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
