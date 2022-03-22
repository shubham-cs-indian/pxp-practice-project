package com.cs.core.config.interactor.model.processdetails;

public interface IProcessStatusModel extends IProcessModel {
  
  public static final String STATUS = "status";
  
  public Boolean getStatus();
  
  public void setStatus(Boolean status);
}
