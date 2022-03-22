package com.cs.workflow.camunda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




/**
 * Camunda operations will be listed here 
 * and should be used through dependency 
 * in the class wherever required
 * 
 * @author mayuri.wankhade
 *
 */
@Component("camundaServiceManager")
public class CamundaService implements IBPMNEngineService {
  
  @Autowired
  private RuntimeService runtimeService;
  
  @Autowired
  private TaskService taskService;
  
  @Override
  public void notifyTask(String taskInstanceId, Map<String, Object> variables) {
    if (taskService.createTaskQuery()
        .taskId(taskInstanceId)
        .active()
        .singleResult() != null)
      taskService.complete(taskInstanceId, variables);
  }
  
  
  public void startProcessInstanceById(String processId, Map<String, Object> parameters) throws ProcessEngineException
  {
    runtimeService.startProcessInstanceById(processId, parameters);
  }
  
  public void signalEventReceived(String eventName, Map<String, Object> parameters) throws ProcessEngineException
  {
    runtimeService.signalEventReceived(eventName, parameters);
  }

  public void createMessageCorrelation(String correlationId, Map<String, Object> parameters) throws ProcessEngineException
  {
    runtimeService.createMessageCorrelation(correlationId)
            .setVariables(parameters)
            .correlateAllWithResult();
  }

  public void correlateMessage(String correlationId) throws ProcessEngineException
  {
    runtimeService.correlateMessage(correlationId);
    
  }
  
  public void startProcessInstanceByIds(Set<String> processIds, Map<String, Object> parameters) {
    for (String processId : processIds) {
      this.startProcessInstanceById(processId, parameters);
    }
  }

  @Override
  public boolean isTaskInstanceRunning(String taskInstanceId)
  {
    List<Task> taskIds = taskService.createTaskQuery().taskId(taskInstanceId).list();  
    return taskIds.size() >0 ;
  }
  
  @Override
  public List<String> getKlassInstanceIdsForRunningProcesses(List<String> ids){
    List<String> idsNotToDelete = new ArrayList<>();
    for (String instanceId : ids) {
      List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
      processInstances = runtimeService.createProcessInstanceQuery().variableValueEquals("klassInstanceId", instanceId).list();
      //List<String> processInstanceIds = processInstances.stream().map(map -> map.getProcessInstanceId()).collect(Collectors.toList());
      if (!processInstances.isEmpty()) {
        idsNotToDelete.add(instanceId);
      }
    }
    return idsNotToDelete;
  }
}
