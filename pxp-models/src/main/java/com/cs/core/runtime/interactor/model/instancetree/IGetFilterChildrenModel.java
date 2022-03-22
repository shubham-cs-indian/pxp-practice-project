package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetFilterChildrenModel extends IModel {
  
  public static final String ID    = "id";
  public static final String LABEL = "label";
  public static final String COUNT = "count";
  public static final String FROM  = "from";
  public static final String TO    = "to";
  public static final String CODE  = "code";
  public static final String ICON  = "icon";
  public static final String ICON_KEY  = "iconKey";
  
  public String getId();
  public void setId(String id);
  
  public String getLabel();
  public void setLabel(String label);
  
  public Long getCount();
  public void setCount(Long count);
  
  public Double getFrom();
  public void setFrom(Double from);
  
  public Double getTo();
  public void setTo(Double to);
  
  public String getCode();
  public void setCode(String code);
  
  public String getIcon();
  public void setIcon(String icon);
  
  public String getIconKey();
  public void setIconKey(String iconKey);
}
