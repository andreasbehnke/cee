package com.cee.news.server.content;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteRepositoryHook {
	
	private static final Logger LOG = LoggerFactory.getLogger(DeleteRepositoryHook.class);
	
	private final static String TEST_REPOSITORY_DIR = "repository";

	private RepositoryImpl repository;
	
	public DeleteRepositoryHook() {}
	
	public RepositoryImpl getRepository() {
		return repository;
	}

	public void setRepository(RepositoryImpl repository) {
		this.repository = repository;
	}
	
	public void deleteRepository() throws IOException {
		if (repository != null) {
			repository.shutdown();
		}
		LOG.info("Trying to delete repository directory");
		FileUtils.deleteDirectory(new File(TEST_REPOSITORY_DIR));
		LOG.info("Deleted repository directory");
	}
}
