package com.cs.core.config.interactor.model.translations;

public class RelationshipTranslationsModel implements IRelationshipTranslationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          label;
  protected String          side1Label;
  protected String          side2Label;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getSide1Label()
  {
    return side1Label;
  }
  
  @Override
  public void setSide1Label(String side1Label)
  {
    this.side1Label = side1Label;
  }
  
  @Override
  public String getSide2Label()
  {
    return side2Label;
  }
  
  @Override
  public void setSide2Label(String side2Label)
  {
    this.side2Label = side2Label;
  }
}
