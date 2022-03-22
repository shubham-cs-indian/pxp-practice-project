package com.cs.workflow.camunda;

import java.util.List;
import java.util.Map;
import java.util.Set;



public interface IBPMNEngineService {
    void startProcessInstanceById(String processId, Map<String, Object> parameters);
    void signalEventReceived(String eventName, Map<String, Object> parameters);
    void createMessageCorrelation(String messageCorrelationId, Map<String, Object> parameters);
     void correlateMessage(String correlationId);
    void startProcessInstanceByIds(Set<String> processIds, Map<String, Object> parameters);
    boolean isTaskInstanceRunning(String taskInstanceId);
    List<String> getKlassInstanceIdsForRunningProcesses(List<String> ids);
    void notifyTask(String taskInstanceId,Map<String, Object> variables);
}
