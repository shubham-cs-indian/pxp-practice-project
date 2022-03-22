package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.entity.variants.IContextInstance;

import java.util.Map;

public interface ISmartDocumentContextDataModel extends IContextInstance {
  
  public static final String LABEL = "label";
  public static final String TAGS  = "tags";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Map<String, ISmartDocumentTagInstanceDataModel> getTags();
  
  public void setTags(Map<String, ISmartDocumentTagInstanceDataModel> tags);
}
