package com.cs.di.workflow.recovery;

import java.util.List;

public interface IProcessInstanceRecovery {
  
  public void updateStuckProcessInstanceStatusDueToRestart(List<String> stuckProcessIds);
  
  public void updateFailedProcessInstanceStatus(List<String> failedProcessIds);
  
}
