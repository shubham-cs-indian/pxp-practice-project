package com.cs.core.config.interactor.model.tabs;

import com.cs.core.config.interactor.entity.tabs.ITab;

public interface ITabModel extends ITab {
  
  public static final String SEQUENCE = "sequence";
  
  public Integer getSequence();
  
  public void setSequence(Integer sequence);
}
