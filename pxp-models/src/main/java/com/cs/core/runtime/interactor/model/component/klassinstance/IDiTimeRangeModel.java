package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDiTimeRangeModel extends IModel {
  
  public static String TO_DATE   = "to";
  public static String FROM_DATE = "from";
  
  public String getTo();
  
  public void setTo(String to);
  
  public String getFrom();
  
  public void setFrom(String from);
}
