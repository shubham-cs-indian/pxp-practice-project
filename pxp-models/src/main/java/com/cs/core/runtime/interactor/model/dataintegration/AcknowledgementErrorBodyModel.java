package com.cs.core.runtime.interactor.model.dataintegration;

public class AcknowledgementErrorBodyModel implements IAcknowledgementErrorBodyModel {
  
  private static final long serialVersionUID = 1L;
  private String            code;
  private String            message;
  private String            minutesBeforeRetry;
  private String            lastUpdateDate;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getMessage()
  {
    return message;
  }
  
  @Override
  public void setMessage(String message)
  {
    this.message = message;
  }
  
  @Override
  public String getMinutesBeforeRetry()
  {
    return minutesBeforeRetry;
  }
  
  @Override
  public void setMinutesBeforeRetry(String minutesBeforeRetry)
  {
    this.minutesBeforeRetry = minutesBeforeRetry;
  }
  
  @Override
  public String getLastUpdateDate()
  {
    return lastUpdateDate;
  }
  
  @Override
  public void setLastUpdateDate(String lastUpdateDate)
  {
    this.lastUpdateDate = lastUpdateDate;
  }
}
