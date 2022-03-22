package com.cs.loadbalancer.inds.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.loadbalancer.inds.exceptions.INDSNotAvailableException;
import com.cs.loadbalancer.inds.task.base.IINDSTask;
import com.cs.loadbalancer.inds.utils.INDSConstants;
import com.cs.loadbalancer.inds.utils.INDSLoadBalancerUtils;

/**
 *	The scheduler implementation. Performs scheduling using the following data structures,
 *  1. Execution map - A map containing the tasks which are currently under execution on the indesign server instances.
 *  2. Task list - A sequential list containing the tasks which are waiting to be executed.   
 *  3. Free servers list - A list containing all the free indesign server instances.
 *  
 *  Note: All the methods inside the scheduler are thread safe. ('synchronized' keyword has been used)
 */
public class INDSScheduler implements IINDSScheduler {

	private AtomicInteger NO_OF_INDS_INSTANCES; //stores the total no. of INDS instances that the scheduler is aware of.
	private Map<String, IINDSTask> executionMap; //stores all the tasks currently under execution.
	private List<IINDSTask> taskList; //stores all the tasks that are waiting to be executed.
	private List<IInDesignServerInstance> allServersList; //contains list of all the indesign server instances that the scheduler is aware of.
	private List<IInDesignServerInstance> freeServersList; //contains a list of free insdesign server instances.

	public INDSScheduler() {
		this.NO_OF_INDS_INSTANCES = new AtomicInteger(0); //initialized as an AtomicInteger for thread safety
		this.executionMap = new ConcurrentHashMap<>(); //initialized as a concurrent hashmap for thread safety
		this.taskList = Collections.synchronizedList(new LinkedList<IINDSTask>()); //initialized as a synchronized list for thread safety
		this.allServersList = Collections.synchronizedList(new ArrayList<IInDesignServerInstance>()); //initialized as a synchronized list for thread safety
		this.freeServersList = Collections.synchronizedList(new ArrayList<IInDesignServerInstance>()); //initialized as a synchronized list for thread safety
	}
	
	/**
	 * This method adds a task to the scheduler. The task is added to execution map or sequential list based on various conditions.
	 */
	@Override
	public synchronized void addTaskToScheduler(IINDSTask task) {
		boolean shouldAddToExecutionMap = (executionMap.size() < NO_OF_INDS_INSTANCES.get()
				&& taskList.isEmpty() && !executionMap.containsKey(task.getExecutionTaskId())) || task.getIsPriorityTask() || NO_OF_INDS_INSTANCES.get() == 0;
		
		if (shouldAddToExecutionMap) {
			addToExecutionMap(task);
		} else {
			taskList.add(task);
		}
	}

	/**
	 * This method removes a task from the scheduler. 
	 * The task is removed from execution map & a new task is picked from the sequential list and added to the execution map.
	 */
	@Override
	public synchronized void removeTaskFromScheduler(IINDSTask task) {
		removeFromExecutionMap(task);
		addATaskFromListToExecutionMap();
	}

	/**
	 * Adds the indesign server instances to the free server list, as well the master servers list & also updates the total no. of inds instances.
	 */
	@Override
	public synchronized void addInDesignServerInstances(
			List<IInDesignServerInstance> serverList) {
		allServersList.addAll(serverList);
		freeServersList.addAll(serverList);
		updateNoOfINDSInstances(serverList.size(), true);
	}
	
	/**
	 * Removes the indesign server instances from the free server list, as well the master servers list & also updates the total no. of inds instances.
	 */
	@Override
	public synchronized void removeInDesignServerInstances(
			List<IInDesignServerInstance> serverList) {
		allServersList.removeAll(serverList);
		freeServersList.removeAll(serverList);
		updateNoOfINDSInstances(serverList.size(), false);
	}

	/**
	 * Reschedules a task & adds it back to the scheduler if the INDS instance which was assigned to it becomes in-active.
	 */
	@Override
	public synchronized void rescheduleTaskIfINDSNotAvailable(IINDSTask task) {
		IInDesignServerInstance inDesignServerInstance = task.getAssignedInDesignServerInstance();
		if(inDesignServerInstance != null) {
			inDesignServerInstance.setStatus(INDSConstants.INDS_IN_ACTIVE);
		}
		removeFromExecutionMap(task);
		addTaskToScheduler(task);
	}
	
	/**
	 * Updates the statuses of the inds instances.
	 */
	@Override
	public synchronized void updateINDSStatuses(
			List<IInDesignServerInstance> indsInstancesWithUpdatedData) {
		for (IInDesignServerInstance indsInstanceWithUpdatedData : indsInstancesWithUpdatedData) {
			int index = allServersList.indexOf(indsInstanceWithUpdatedData);
			if (index != -1) {
				IInDesignServerInstance indsInstanceToModify = allServersList.get(index);
				indsInstanceToModify.setStatus(indsInstanceWithUpdatedData.getStatus());
			}
		}
	}
	
	/**
   * Updates the statuses of the inds instances and adds in active list if it is not added.
   */
  @Override
  public synchronized void updateAndAddINDS(
      List<IInDesignServerInstance> indsInstancesWithUpdatedData) {
    for (IInDesignServerInstance indsInstanceWithUpdatedData : indsInstancesWithUpdatedData) {
      int index = allServersList.indexOf(indsInstanceWithUpdatedData);
      if (index != -1) {
        IInDesignServerInstance indsInstanceToModify = allServersList.get(index);
        indsInstanceToModify.setStatus(indsInstanceWithUpdatedData.getStatus());
      } else {
        allServersList.add(indsInstanceWithUpdatedData);
        freeServersList.add(indsInstanceWithUpdatedData);
        updateNoOfINDSInstances(1, true);
      }
    }
  }
	
	/**
	 * Updates the total no. of INDS instances.
	 */
  private synchronized void updateNoOfINDSInstances(int noOfINDSInstances, boolean shouldAdd)
  {
    noOfINDSInstances = shouldAdd ? noOfINDSInstances + NO_OF_INDS_INSTANCES.get() : 
      NO_OF_INDS_INSTANCES.get() > noOfINDSInstances ? NO_OF_INDS_INSTANCES.get() - noOfINDSInstances : 0;
    NO_OF_INDS_INSTANCES.set(noOfINDSInstances);
  }
	
	/**
	 * Adds a task to execution map & starts it's execution.
	 */
	private synchronized void addToExecutionMap(IINDSTask task) {
		if(freeServersList.isEmpty() && !task.getIsConfigurationTask()) {
			INDSLoadBalancerUtils.sendErrorResponse(task.getAsyncContext(), new INDSNotAvailableException());
			return;
		}
		
		if (!task.getIsConfigurationTask()) {
			task.assignInDesignServerInstance(freeServersList.remove(0));
		}

		executionMap.put(task.getExecutionTaskId(), task);
		Thread taskThread = new Thread(task);
		taskThread.start();
	}

	/**
	 * Removes a task from execution map & frees up the inds instance (if the task is assigned an instance).
	 */
	private synchronized void removeFromExecutionMap(IINDSTask task) {
		executionMap.remove(task.getExecutionTaskId());
		IInDesignServerInstance inDesignServerInstance = task.getAssignedInDesignServerInstance();
		
		boolean shouldAddToFreeServerList = (inDesignServerInstance != null
				&& !task.getIsConfigurationTask()
				&& !inDesignServerInstance.getStatus().equals(INDSConstants.INDS_IN_ACTIVE))
				&& allServersList.contains(inDesignServerInstance);
	
		if (shouldAddToFreeServerList) {
			freeServersList.add(inDesignServerInstance);
		}
	}
	
	/**
	 * Adds a task from list to the execution map.
	 */
	private synchronized void addATaskFromListToExecutionMap() {
		IINDSTask taskToExecute = null;
		
		for (IINDSTask task : taskList) {
			if (!executionMap.containsKey(task.getExecutionTaskId())) {
				taskToExecute = task;
				break;
			}
		}

		if (taskToExecute != null) {
			taskList.remove(taskToExecute);
			addToExecutionMap(taskToExecute);
		}
	}

}