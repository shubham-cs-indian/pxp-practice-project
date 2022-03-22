package com.cs.core.config.businessapi.task;

import com.cs.core.config.interactor.model.task.IPropagateTaskChangesModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.JobData;
import com.cs.core.runtime.interactor.model.logger.JobThreadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class PropagateTaskChangesToTaskInstancesTask implements Callable<IIdParameterModel> {
  
  protected IPropagateTaskChangesModel propagateTaskChangesModel;
  
  protected String                     parentTransactionId;
  
  /*@Autowired
  protected IPropagateTaskChangesToTaskInstanceStrategy propagateTaskChangesToTaskInstanceStrategy;*/
  
  @Autowired
  protected JobThreadData              jobThread;
  
  public void setData(IPropagateTaskChangesModel propagateTaskChangesModel, String transactionId)
  {
    this.propagateTaskChangesModel = propagateTaskChangesModel;
    parentTransactionId = transactionId;
  }
  
  @Override
  public IIdParameterModel call() throws Exception
  {
    IIdParameterModel returnModel = new IdParameterModel();
    try {
      JobData jobData = new JobData();
      jobData.setParentTransactionId(parentTransactionId);
      jobThread.setJobData(jobData);
      // returnModel =
      // propagateTaskChangesToTaskInstanceStrategy.execute(propagateTaskChangesModel);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    // return returnModel;
    return null;
  }
}
