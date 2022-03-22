package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISmartDocumentTagValueDataModel extends IModel {
  
  public static final String LABEL     = "label";
  public static final String RELEVANCE = "relevance";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Integer getRelevance();
  
  public void setRelevance(Integer relevance);
}
