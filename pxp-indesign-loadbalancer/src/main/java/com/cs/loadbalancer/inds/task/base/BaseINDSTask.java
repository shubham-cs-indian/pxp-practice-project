package com.cs.loadbalancer.inds.task.base;

import java.util.UUID;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.loadbalancer.inds.exceptions.INDSNotAvailableException;
import com.cs.loadbalancer.inds.scheduler.IINDSScheduler;
import com.cs.loadbalancer.inds.utils.INDSLoadBalancerUtils;
import com.cs.runtime.interactor.model.indsserver.IINDSTaskRequestModel;

/**
 * The base class for Tasks. Implements the runnable interface & thus makes it possible for tasks to be executed as threads.
 * Contains the common data structures & methods needed by all the child task classes. 
 */
public abstract class BaseINDSTask implements IINDSTask {

	protected String executionTaskId;
	protected IINDSScheduler scheduler;
	protected IINDSTaskRequestModel indsTaskRequestModel;
	protected IInDesignServerInstance inDesignServerInstance;
	protected Boolean isConfigurationTask;
	protected Boolean isPriorityTask;
	protected AsyncContext asyncContext;

	public abstract void performTask() throws Exception; // this method is implemented in the child classes

	public BaseINDSTask(AsyncContext asyncContext, IINDSScheduler scheduler, IINDSTaskRequestModel indsTaskRequestModel) {
		this.executionTaskId = indsTaskRequestModel.getTaskId() != null ? indsTaskRequestModel.getTaskId() : UUID.randomUUID().toString();
		this.isConfigurationTask = false;
		this.isPriorityTask = false;
		this.scheduler = scheduler;
		this.indsTaskRequestModel = indsTaskRequestModel;
		this.asyncContext = asyncContext;
	}

	/**
	 * This method is called first when the task is executed as a thread.
	 * It calls the 'performTask' method implemented in the respective child classes.
	 */
	@Override
	public void run() {
		try {
			performTask();
		} catch (Exception ex) {
			if (ex instanceof INDSNotAvailableException) {
				scheduler.rescheduleTaskIfINDSNotAvailable(this);
				return;
			} else {
				INDSLoadBalancerUtils.sendErrorResponse(asyncContext, ex);
				scheduler.removeTaskFromScheduler(this);
				return;
			}
		}

		scheduler.removeTaskFromScheduler(this);

		HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

		long time = System.currentTimeMillis();
		response.setStatus(HttpServletResponse.SC_OK);
		response.setDateHeader("Date", time);
		response.setDateHeader("Last-Modified", time);
		asyncContext.complete();  //async context needs to be completed to send back the response.
	}

	@Override
	public String getExecutionTaskId() {
		return executionTaskId;
	}

	@Override
	public Boolean getIsConfigurationTask() {
		return isConfigurationTask;
	}

	@Override
	public Boolean getIsPriorityTask() {
		return isPriorityTask;
	}
	
	@Override
	public AsyncContext getAsyncContext() {
		return asyncContext;
	}

	@Override
	public IInDesignServerInstance getAssignedInDesignServerInstance() {
		return this.inDesignServerInstance;
	}

	@Override
	public void assignInDesignServerInstance(
			IInDesignServerInstance inDesignServerInstance) {
		this.inDesignServerInstance = inDesignServerInstance;
	}

}