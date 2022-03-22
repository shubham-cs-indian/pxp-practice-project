package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAcknowledgementPropertiesModel extends IModel {
  
  public static final String ID_TRANSACTION = "IDtransaction";
  public static final String SOURCE         = "source";
  public static final String IS_LAST_RETRY  = "isLastRetry";
  
  public String getIDtransaction();
  
  public void setIDtransaction(String IDtransaction);
  
  public String getSource();
  
  public void setSource(String source);
  
  public String getIsLastRetry();
  
  public void setIsLastRetry(String isLastRetry);
}
