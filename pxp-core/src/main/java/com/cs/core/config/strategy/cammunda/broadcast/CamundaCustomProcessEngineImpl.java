package com.cs.core.config.strategy.cammunda.broadcast;

import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class CamundaCustomProcessEngineImpl extends ProcessEngineImpl {
  
  public CamundaCustomProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration)
  {
    
    super(processEngineConfiguration);
    // processEngineConfiguration.getJobExecutor().setLockTimeInMillis(12000000);
    // TODO Auto-generated constructor stub
  }
}
