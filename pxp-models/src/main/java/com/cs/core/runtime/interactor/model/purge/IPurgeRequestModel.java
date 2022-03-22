package com.cs.core.runtime.interactor.model.purge;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPurgeRequestModel extends IModel {
  
  public static final String DOC_TYPE   = "docType";
  public static final String PURGE_TIME = "purgeTime";
  
  public String getDocType();
  
  public void setDocType(String docType);
  
  public Long getPurgeTime();
  
  public void setPurgeTime(Long purgeTime);
}
