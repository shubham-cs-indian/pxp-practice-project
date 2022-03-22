package com.cs.workflow.base;

import java.util.List;
import java.util.Map;

public interface IAbstractExecutionReader {
  public static String RUNTIME_VARIABLES = "runtimeVariables";
  
  Object getVariable(Object execution, String key);
  
  void setVariable(Object execution, String key, Object value);
  
  String getProcessInstanceId(Object execution);

  String getActivityName(Object execution);
  
  String getProcessDefinitionId(Object execution);
  
  String getActivityInstanceId(Object execution);
  
  Map<String, Map<String, Object>> readTaskVariablesMap(String processDefinition, String label);

  String updateBPMN(String processDefinition, String runtimeVariables);
  
  Map<String, String> getProperties(Object execution);

  abstract List<Map<String, Object>> getFormFields(Object execution);

  abstract String getId(Object execution);
  
  Map<String, Map<String, Object>> getAllVariables(String processDefinition);
}