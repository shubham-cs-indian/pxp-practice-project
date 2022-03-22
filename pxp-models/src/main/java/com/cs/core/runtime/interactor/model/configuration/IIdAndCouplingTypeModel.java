package com.cs.core.runtime.interactor.model.configuration;

public interface IIdAndCouplingTypeModel extends IModel {
  
  public static String ID            = "id";
  public static String COUPLING_TYPE = "couplingType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
}
