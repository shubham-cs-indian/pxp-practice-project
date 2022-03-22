package com.cs.core.config.interactor.entity.task;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface ITask extends IConfigEntity {
  
  public static final String COLOR        = "color";
  public static final String LABEL        = "label";
  public static final String ICON         = "icon";
  public static final String ICON_KEY     = "iconKey";
  public static final String PRIORITY_TAG = "priorityTag";
  public static final String STATUS_TAG   = "statusTag";
  public static final String TYPE         = "type";
  
  public String getColor();
  
  public void setColor(String color);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
 public String getIconKey();
  
  public void setIconKey(String iconKey);
  
  public String getPriorityTag();
  
  public void setPriorityTag(String priorityTag);
  
  public String getStatusTag();
  
  public void setStatusTag(String statusTag);
  
  public String getType();
  
  public void setType(String type);
}
