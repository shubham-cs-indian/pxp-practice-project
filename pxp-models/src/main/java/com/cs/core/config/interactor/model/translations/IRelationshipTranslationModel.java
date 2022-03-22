package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRelationshipTranslationModel extends IModel {
  
  public static final String LABEL        = "label";
  public static final String SIDE_1_LABEL = "side1Label";
  public static final String SIDE_2_LABEL = "side2Label";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getSide1Label();
  
  public void setSide1Label(String side1Label);
  
  public String getSide2Label();
  
  public void setSide2Label(String side2Label);
}
