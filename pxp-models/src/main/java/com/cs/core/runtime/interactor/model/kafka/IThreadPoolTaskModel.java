package com.cs.core.runtime.interactor.model.kafka;

import java.io.Serializable;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;


public interface IThreadPoolTaskModel extends Serializable {
  public static final String CONTENT_INFO     = "contentInfo";
  public static final String TASK_NAME        = "taskName";
  public static final String CONTENT_ID       = "contentId";
  public static final String TRANSACTION_DATA = "transactionData";
  public static final String RETRY_COUNT      = "retryCount";
  
  public IModel getContentInfo();
  
  public void setContentInfo(IModel contentInfo);
  
  public String getTaskName();
  
  public void setTaskName(String taskName);
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public ITransactionData getTransactionData();
  
  public void setTransactionData(ITransactionData transactionData);
  
  public Integer getRetryCount();
  
  public void setRetryCount(Integer retryCount);

}
