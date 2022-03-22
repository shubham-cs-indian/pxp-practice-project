package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKafkaDataLogModel extends IModel {
  
  public static final String ID             = "id";
  public static final String START_TIME     = "startTime";
  public static final String TRANSACTION_ID = "transactionId";
  public static final String STATUS         = "status";
  public static final String TASK           = "task";
  public static final String ERROR_MESSAGE  = "errorMessage";
  public static final String ENTITY_ID      = "entityId";
  public static final String USER_ID        = "userId";
  public static final String LEVEL          = "level";
  
  public String getId();
  
  public void setId(String id);
  
  public Long getStartTime();
  
  public void setStartTime(Long startTime);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getTransactionId();
  
  public void setTransactionId(String transactionId);
  
  public String getStatus();
  
  public void setStatus(String status);
  
  public String getTask();
  
  public void setTask(String task);
  
  public String getErrorMessage();
  
  public void setErrorMessage(String errorMessage);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Long getLevel();
  
  public void setLevel(Long level);
}
