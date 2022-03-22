package com.cs.core.runtime.interactor.model.dataintegration;

public class JMSExportAcknowledgementStatusModel implements IJMSExportAcknowledgementStatusModel {
  
  private static final long serialVersionUID = 1L;
  private String            idTransaction    = "";
  private Boolean           isAcknowledged   = false;
  
  @Override
  public String getIdTransaction()
  {
    return idTransaction;
  }
  
  @Override
  public void setIdTransaction(String idTransaction)
  {
    this.idTransaction = idTransaction;
  }
  
  @Override
  public Boolean getIsAcknowledged()
  {
    return isAcknowledged;
  }
  
  @Override
  public void setIsAcknowledged(Boolean isAcknowledged)
  {
    this.isAcknowledged = isAcknowledged;
  }
}
