package com.cs.workflow.base;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Representation of workflow tasks
 *  
 * @author ruplesh.pawar
 */
public interface IAbstractTask {

  /**
   * @param execution
   * @param reader
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void execute(Object execution, IAbstractExecutionReader reader)
          throws JsonParseException, JsonMappingException, IOException;
  /**
   * @return input parameters required for task
   */
  public List<String> getInputList();

  /**
   * @return output parameters generated after execution of task
   */
  public List<String> getOutputList();

  /**
   * @return workflow types a task belongs to
   */
  public List<WorkflowType> getWorkflowTypes();
  
  /**
   * @return events types a task belongs to
   */
  public List<EventType> getEventTypes();
  
  /**
   * @return type of task
   */
  public TaskType getTaskType();
  
  /**
   * Validate input parameters
   * @param inputFields
   */
  List<String> validate(Map<String, Object> inputFields);

}
