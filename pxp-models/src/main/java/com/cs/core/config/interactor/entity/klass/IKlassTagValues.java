package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IKlassTagValues extends IEntity {
  
  public static final String TAG_ID = "tagId";
  public static final String LABEL  = "label";
  public static final String COLOR  = "color";
  public static final String ICON   = "icon";
  
  public String getTagId();
  
  public void setTagId(String tagValueId);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getColor();
  
  public void setColor(String color);
  
  public String getIcon();
  
  public void setIcon(String icon);
}
