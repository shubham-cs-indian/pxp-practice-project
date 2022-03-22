package com.cs.core.runtime.interactor.model.smartdocument;

public class SmartDocumentTagValueDataModel implements ISmartDocumentTagValueDataModel {
  
  private static final long serialVersionUID = 1L;
  protected String          label;
  protected Integer         relevance;
  
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
  public Integer getRelevance()
  {
    return relevance;
  }
  
  @Override
  public void setRelevance(Integer relevance)
  {
    this.relevance = relevance;
  }
}
