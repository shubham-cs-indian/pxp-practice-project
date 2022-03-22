package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.strategy.model.kafka.IQueuedRequestModel;

import java.io.Serializable;

public class QueuedRequestModel implements Serializable, IQueuedRequestModel {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  protected String          jobId;
  
  protected IModel          model;
  
  public QueuedRequestModel(String jobId, IModel model)
  {
    this.jobId = jobId;
    this.model = model;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IQueuedRequestModel#getJobId()
   */
  @Override
  public String getJobId()
  {
    return jobId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IQueuedRequestModel#setJobId(java.lang.String)
   */
  @Override
  public void setJobId(String jobId)
  {
    this.jobId = jobId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IQueuedRequestModel#getModel()
   */
  @Override
  public IModel getModel()
  {
    return model;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IQueuedRequestModel#setModel(com.cs.base.interactor.model.IModel)
   */
  @Override
  public void setModel(IModel model)
  {
    this.model = model;
  }
}
