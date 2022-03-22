package com.cs.core.config.interactor.entity.tag;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface ITagValue extends IConfigEntity {
  
  public static final String LABEL     = "label";
  public static final String COLOR     = "color";
  public static final String RELEVANCE = "relevance";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getColor();
  
  public void setColor(String color);
  
  public Integer getRelevance();
  
  public void setRelevance(Integer relevance);
}
