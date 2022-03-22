package com.cs.core.config.strategy.cammunda.broadcast;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;

public class CamundaCustomSpringProcessEngineConfiguration
    extends SpringProcessEngineConfiguration {
  
  @Value("${camunda.lockTimeInMillis}")
  int lockTimeInMillis;
  
  @Override
  public ProcessEngine buildProcessEngine()
  {
    init();
    processEngine = new CamundaCustomProcessEngineImpl(this);
    JobExecutor je = processEngine.getProcessEngineConfiguration()
        .getJobExecutor();
    je.setLockTimeInMillis(lockTimeInMillis);
    //je.setWaitTimeInMillis(150000);
    processEngine.getProcessEngineConfiguration()
        .setJobExecutor(je);
    
    invokePostProcessEngineBuild(processEngine);
    return processEngine;
  }
}
