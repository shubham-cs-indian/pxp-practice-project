package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;

public class TaskUtil {
  
  public enum TaskStatus
  {
    TASKPLANNED("taskplanned"), TASKREADY("taskready"), TASKINPROGRESS("taskinprogress"),
    TASKDONE("taskdone"), TASKVERIFIED("taskverified"), TASKSIGNEDOFF("tasksignedoff"),
    TASKDECLINED("taskdeclined");
    
    private String taskStatus;
    
    private static final TaskStatus[] values = values();
    
    private TaskStatus(String taskStatus)
    {
      this.taskStatus = taskStatus;
    }
    
    public String getTaskStatus()
    {
      return taskStatus;
    }
    
    public static int getOrdinal(String taskStatus)
    {
      int ordinal = 0;
      for (TaskStatus tskStatus : values) {
        if (tskStatus.getTaskStatus()
            .equalsIgnoreCase(taskStatus)) {
          ordinal = tskStatus.ordinal();
        }
      }
      return ordinal;
    }
    
    public static TaskStatus valueOf(int ordinal) {
      return values[ordinal];
    }
  }
  
  public static TaskType getTakType(String taskTypeStr)
  {
    TaskType taskType = TaskType.UNDEFINED;
    
    switch (taskTypeStr) {
      
      case CommonConstants.TASK_TYPE_PERSONAL:
        taskType = TaskType.PERSONAL;
        break;
      case CommonConstants.TASK_TYPE_SHARED:
        taskType = TaskType.SHARED;
        break;
      default:
        taskType = TaskType.UNDEFINED;
        
    }
    return taskType;
  }
  
  public static RACIVS getRACIVS(String string)
  {
    RACIVS vRACIVS = RACIVS.UNDEFINED;
    switch (string) {
      case "responsible":
        vRACIVS = RACIVS.RESPONSIBLE;
        break;
      case "accountable":
        vRACIVS = RACIVS.ACCOUNTABLE;
        break;
      case "consulted":
        vRACIVS = RACIVS.CONSULTED;
        break;
      case "informed":
        vRACIVS = RACIVS.INFORMED;
        break;
      case "verify":
        vRACIVS = RACIVS.VERIFIER;
        break;
      case "signoff":
        vRACIVS = RACIVS.SIGNOFF;
        break;
      default:
        vRACIVS = RACIVS.UNDEFINED;
    }
    return vRACIVS;
  }
  
}
