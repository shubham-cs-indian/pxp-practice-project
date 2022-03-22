package com.cs.di.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.strategy.utils.DbUtils;
import com.cs.di.workflow.recovery.IProcessInstanceRecovery;

@Component
public class CamundaWorkflowRecoveryService {
  
  @Autowired
  protected ProcessEngine                                 processEngine;
  
  @Autowired
  protected DbUtils                                       dbUtils;
  
  @Autowired
  protected IProcessInstanceRecovery processInstanceRecovery;
  
  @Value("${camunda.unlock-stuck-jobs}")
  protected Boolean                                       unlockStuckJobs;
  
  // @Value("${camunda.retry-failed-jobs}")
  protected Boolean                                       retryFailedJobs = false;
  
  @Value("${camunda.update-stuck-and-failed-jobs-status}")
  protected Boolean                                       updateStuckAndFailedJobsStatus;
    
  @PostConstruct
  public void executeRecovery()
  {
    if (updateStuckAndFailedJobsStatus) {
      updateStuckProcessInstancesStatusDueToRestart();
      updateFailedProcessInstanceStatus();
    }
    if (unlockStuckJobs) {
      unlockStuckProcessInstances();
    }
    if (retryFailedJobs) {
      retryFailedProcessInstances();
    }
  }
  
  private void updateStuckProcessInstancesStatusDueToRestart()
  {
    updateStuckProcessInstancesStatusDueToRestart((String) null);
  }
  
  private void updateStuckProcessInstancesStatusDueToRestart(String processDefKey)
  {
    try {
      List<String> collumnsToSelect = new ArrayList<String>();
      collumnsToSelect.add("process_instance_id_");
      
      List<String> notNullConditionalCollumns = new ArrayList<String>();
      notNullConditionalCollumns.add("lock_exp_time_");
      notNullConditionalCollumns.add("lock_owner_");
      
      Map<String, Object> conditionalColumnValues = new HashMap<String, Object>();
      conditionalColumnValues.put("retries_", 1);
      
      if (processDefKey != null) {
        conditionalColumnValues.put("process_def_key_", processDefKey);
      }
      
      List<Map<String, Object>> result = dbUtils.selectQueryWithNotNull("act_ru_job",
          collumnsToSelect, conditionalColumnValues, notNullConditionalCollumns);
      
      List<String> stuckProcessIds = result.stream()
          .map(entry -> entry.get("process_instance_id_").toString())
          .collect(Collectors.toList());
      
      if (!stuckProcessIds.isEmpty()) {
        processInstanceRecovery.updateStuckProcessInstanceStatusDueToRestart(stuckProcessIds);
      }
    }
    catch (Exception e) {
      System.out.println("### Stuck workflow process instances status update failed ###");
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }
  
  private void updateFailedProcessInstanceStatus()
  {
    updateFailedProcessInstanceStatus(null);
  }

  private void updateFailedProcessInstanceStatus(String processDefKey)
  {
    try {
      List<String> collumnsToSelect = new ArrayList<String>();
      collumnsToSelect.add("process_instance_id_");
            
      Map<String, Object> conditionalColumnValues = new HashMap<String, Object>();
      conditionalColumnValues.put("retries_", 0);
      
      if (processDefKey != null) {
        conditionalColumnValues.put("process_def_key_", processDefKey);
      }
      
      List<Map<String, Object>> result = dbUtils.selectQuery("act_ru_job",
          collumnsToSelect, conditionalColumnValues);
      
      List<String> failedProcessInstanceIds = result.stream()
          .map(entry -> entry.get("process_instance_id_").toString())
          .collect(Collectors.toList());
      
      if (!failedProcessInstanceIds.isEmpty()) {
        processInstanceRecovery.updateFailedProcessInstanceStatus(failedProcessInstanceIds);
      }
    }
    catch (Exception e) {
      System.out.println("### Failed workflow process instances status update failed ###");
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }
  
  public void unlockStuckProcessInstances()
  {
    unlockStuckProcessInstances(null);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void unlockStuckProcessInstances(String processDefKey)
  {
    try {
      Map<String, Object> columnValuesToSet = new HashMap<String, Object>();
      columnValuesToSet.put("lock_exp_time_", null);
      columnValuesToSet.put("lock_owner_", null);
      
      Map<String, Object> conditionalColumnValue = new HashMap<String, Object>();
      conditionalColumnValue.put("retries_", 1);
      
      if (processDefKey != null) {
        conditionalColumnValue.put("process_def_key_", processDefKey);
      }
      
      dbUtils.updateQueryWithNotNull("act_ru_job", columnValuesToSet, conditionalColumnValue,
          new ArrayList(columnValuesToSet.keySet()));
      System.out.println("@#@ #@# Stuck workflow process instnaces unlocked sucessfully #@# @#@");
    }
    catch (Exception e) {
      System.out.println("### Stuck workflow process instances unlock failed ###");
      e.printStackTrace();
      RDBMSLogger.instance()
          .exception(e);
    }
  }
  
  public void retryFailedProcessInstances()
  {
    retryFailedProcessInstances(null);
  }
  
  private void retryFailedProcessInstances(String processDefKey)
  {
    try {
      Map<String, Object> columnValuesToSet = new HashMap<String, Object>();
      columnValuesToSet.put("retries_", 1);
      
      Map<String, Object> conditionalColumnValue = new HashMap<String, Object>();
      conditionalColumnValue.put("retries_", 0);
      
      if (processDefKey != null) {
        conditionalColumnValue.put("process_def_key_", processDefKey);
      }
      
      dbUtils.updateQuery("act_ru_job", columnValuesToSet, conditionalColumnValue);
      System.out.println("@#@ #@# Failed workflow process instances retried sucessfully #@# @#@");
    }
    catch (Exception e) {
      System.out.println("### Failed workflow process instances retry failed ###");
      e.printStackTrace();
      RDBMSLogger.instance()
          .exception(e);
    }
  }
  
  public Boolean getUnlockStuckJobs()
  {
    return unlockStuckJobs;
  }
  
  public void setUnlockStuckJobs(Boolean unlockStuckJobs)
  {
    this.unlockStuckJobs = unlockStuckJobs;
  }
  
  public Boolean getRetryFailedJobs()
  {
    return retryFailedJobs;
  }
  
  public void setRetryFailedJobs(Boolean retryFailedJobs)
  {
    this.retryFailedJobs = retryFailedJobs;
  }
  
}
