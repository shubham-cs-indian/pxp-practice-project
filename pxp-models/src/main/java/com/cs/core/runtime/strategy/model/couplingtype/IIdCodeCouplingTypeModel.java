package com.cs.core.runtime.strategy.model.couplingtype;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;

public interface IIdCodeCouplingTypeModel extends IIdAndCouplingTypeModel {
  
  public static String CODE = "code";
  
  public String getCode();
  
  public void setCode(String code);
}
