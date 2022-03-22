package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.strategy.model.kafka.IJobModel;
import com.cs.core.runtime.strategy.model.kafka.IQueuedRequestModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.concurrent.ThreadLocalRandom;

public class JobModel implements IModel, IJobModel {
  
  /**
   *
   */
  private static final long     serialVersionUID = 1L;
  
  protected String              id;
  
  protected String              targetBeanName;
  
  protected IQueuedRequestModel requestModel;
  
  protected Integer             producerId;
  
  public JobModel(String id, String targetBean, IModel requestModel)
  {
    this.id = id;
    this.targetBeanName = targetBean;
    this.requestModel = new QueuedRequestModel(id, requestModel);
    this.producerId = ThreadLocalRandom.current()
        .nextInt(1, 201);
  }
  
  public JobModel(String id, String targetBean, IModel requestModel, Integer producerId)
  {
    this.id = id;
    this.targetBeanName = targetBean;
    this.requestModel = new QueuedRequestModel(id, requestModel);
    this.producerId = producerId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#getId()
   */
  @Override
  public String getId()
  {
    return id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#setId(java.lang.String)
   */
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#getTargetBeanName()
   */
  @Override
  public String getTargetBeanName()
  {
    return targetBeanName;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#setTargetBeanName(java.lang.String)
   */
  @Override
  public void setTargetBeanName(String targetBeanName)
  {
    this.targetBeanName = targetBeanName;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#getRequestModel()
   */
  @Override
  public IQueuedRequestModel getRequestModel()
  {
    return requestModel;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#setRequestModel(com.cs.base.interactor.model.QueuedRequestModel)
   */
  @Override
  @JsonDeserialize(as = QueuedRequestModel.class)
  public void setRequestModel(IQueuedRequestModel requestModel)
  {
    this.requestModel = requestModel;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#getProducerId()
   */
  @Override
  public Integer getProducerId()
  {
    return producerId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.base.interactor.model.IJobModel#setProducerId(java.lang.Integer)
   */
  @Override
  public void setProducerId(Integer producerId)
  {
    this.producerId = producerId;
  }
}
