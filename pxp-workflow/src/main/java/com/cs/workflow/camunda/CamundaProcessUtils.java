package com.cs.workflow.camunda;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("camundaProcessUtils")
public class CamundaProcessUtils {
  
  @Autowired
  HistoryService historyService;
  
  public void getCompletedActivity(String processInstanceId, ArrayList<String> completedActivityIds)
  {
    List<HistoricActivityInstance> finishedActivities = historyService.createHistoricActivityInstanceQuery()
        .processInstanceId(processInstanceId).finished().list();
    
    for (HistoricActivityInstance finishedActivitie : finishedActivities) {
      completedActivityIds.add(finishedActivitie.getActivityId().toString());
    }
  }
  
  public void getCurrentActivity(String processInstanceId, ArrayList<String> currentActivityIds)
  {
    List<HistoricActivityInstance> currentActivitys = historyService.createHistoricActivityInstanceQuery()
        .processInstanceId(processInstanceId).unfinished().list();
    
    for (HistoricActivityInstance currentActivity : currentActivitys) {
      currentActivityIds.add(currentActivity.getActivityId().toString());
    }
  }
  
}