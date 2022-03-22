package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITimeRangeExportModel extends IModel {
  
  public static String FROM = "from";
  public static String TO   = "to";
  
  public String getFrom();
  
  public void setFrom(String from);
  
  public String getTo();
  
  public void setTo(String to);
}
