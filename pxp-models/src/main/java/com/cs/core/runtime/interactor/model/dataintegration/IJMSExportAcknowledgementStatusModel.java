package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IJMSExportAcknowledgementStatusModel extends IModel {
  
  public static final String ID_TRANSACTION  = "idTransaction";
  public static final String IS_ACKNOWLEDGED = "isAcknowledged";
  
  public String getIdTransaction();
  
  public void setIdTransaction(String idTransaction);
  
  public Boolean getIsAcknowledged();
  
  public void setIsAcknowledged(Boolean isAcknowledged);
}
