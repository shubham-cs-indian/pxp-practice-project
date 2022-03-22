package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBasicTranslationModel extends IModel {
  
  public static final String LABEL = "label";
  
  public String getLabel();
  
  public void setLabel(String label);
}
