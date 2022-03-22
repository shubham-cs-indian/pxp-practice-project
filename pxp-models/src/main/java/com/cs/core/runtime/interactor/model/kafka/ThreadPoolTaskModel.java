package com.cs.core.runtime.interactor.model.kafka;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;


public class ThreadPoolTaskModel implements IThreadPoolTaskModel {
  
  private static final long  serialVersionUID = 1L;
  protected IModel           contentInfo;
  protected String           taskName;
  protected String           contentId;
  protected ITransactionData transactionData;
  protected Integer          retryCount;
  
  @Override
  public IModel getContentInfo()
  {
    return contentInfo;
  }
  
  @Override
  public void setContentInfo(IModel contentInfo)
  {
    this.contentInfo = contentInfo;
  }
  
  @Override
  public String getTaskName()
  {
    return taskName;
  }
  
  @Override
  public void setTaskName(String taskName)
  {
    this.taskName = taskName;
  }
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public ITransactionData getTransactionData()
  {
    return transactionData;
  }
  
  @Override
  public void setTransactionData(ITransactionData transactionData)
  {
    this.transactionData = transactionData;
  }
  
  @Override
  public Integer getRetryCount()
  {
    return this.retryCount;
  }
  
  @Override
  public void setRetryCount(Integer retryCount)
  {
    this.retryCount = retryCount;
  }
  
}
