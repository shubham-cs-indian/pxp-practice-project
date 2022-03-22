package com.cs.di.workflow.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("BGPTask")
public class BGPTask extends AbstractBGPTask {
  
  public static final List<String>       INPUT_LIST     = Arrays.asList(SERVICE, SERVICE_DATA, PRIORITY);
  
  public static final List<String>       OUTPUT_LIST    = Arrays.asList(JOB_ID, EXECUTION_STATUS);
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  /**
   * Validate input parameters
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    return null;
  }
}