package com.cs.core.config.interactor.model.translations;

public class BasicTranslationModel implements IBasicTranslationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          label;
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
}
