package com.cs.loadbalancer.inds.scheduler;

import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.loadbalancer.inds.task.base.IINDSTask;

/**
 * Interface corresponding to the scheduler.
 */
public interface IINDSScheduler {
	public void addTaskToScheduler(IINDSTask task);
	public void removeTaskFromScheduler(IINDSTask task);

	public void addInDesignServerInstances(List<IInDesignServerInstance> serverList);
	public void removeInDesignServerInstances(List<IInDesignServerInstance> serverList);

	public void rescheduleTaskIfINDSNotAvailable(IINDSTask task);
	public void updateINDSStatuses(List<IInDesignServerInstance> indsInstancesWithUpdatedData);
	public void updateAndAddINDS(List<IInDesignServerInstance> indsInstancesWithUpdatedData);
}