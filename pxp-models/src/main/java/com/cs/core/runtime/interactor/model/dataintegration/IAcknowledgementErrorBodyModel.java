package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAcknowledgementErrorBodyModel extends IModel {
  
  public static final String CODE                  = "code";
  public static final String MESSAGE               = "message";
  public static final String MINIUTES_BEFORE_RETRY = "minutesBeforeRetry";
  public static final String LAST_UPDATE_DATE      = "lastUpdateDate";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getMessage();
  
  public void setMessage(String message);
  
  public String getMinutesBeforeRetry();
  
  public void setMinutesBeforeRetry(String minutesBeforeRetry);
  
  public String getLastUpdateDate();
  
  public void setLastUpdateDate(String lastUpdateDate);
}
