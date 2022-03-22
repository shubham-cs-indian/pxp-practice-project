package com.cs.workflow.base;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WorkflowType
{  
  STANDARD_WORKFLOW, SCHEDULED_WORKFLOW, TASKS_WORKFLOW, HIDDEN_WORKFLOW, JMS_WORKFLOW, USER_SCHEDULED_WORKFLOW;
  
  public final static List<String> WORKFLOW_TYPE_LIST = Stream.of(WorkflowType.values()).map(Enum::name).collect(Collectors.toList());
}
