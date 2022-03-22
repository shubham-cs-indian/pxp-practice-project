package com.cs.core.runtime.interactor.model.dataintegration;

public class AcknowledgementPropertiesModel implements IAcknowledgementPropertiesModel {
  
  private static final long serialVersionUID = 1L;
  private String            IDtransaction;
  private String            source;
  private String            isLastRetry;
  
  @Override
  public String getIDtransaction()
  {
    return IDtransaction;
  }
  
  @Override
  public void setIDtransaction(String IDtransaction)
  {
    this.IDtransaction = IDtransaction;
  }
  
  @Override
  public String getSource()
  {
    return source;
  }
  
  @Override
  public void setSource(String source)
  {
    this.source = source;
  }
  
  @Override
  public String getIsLastRetry()
  {
    return isLastRetry;
  }
  
  @Override
  public void setIsLastRetry(String isLastRetry)
  {
    this.isLastRetry = isLastRetry;
  }
}
