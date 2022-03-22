package com.cs.loadbalancer.inds.task.base;

import javax.servlet.AsyncContext;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;

/**
 * The base interface for all the tasks.
 */
public interface IINDSTask extends Runnable {

	public String getExecutionTaskId();
	
	public Boolean getIsConfigurationTask();
	
	public Boolean getIsPriorityTask();
	
	public void performTask() throws Exception;

	public IInDesignServerInstance getAssignedInDesignServerInstance();

	public void assignInDesignServerInstance(IInDesignServerInstance inDesignServerInstance);

	public AsyncContext getAsyncContext();

}