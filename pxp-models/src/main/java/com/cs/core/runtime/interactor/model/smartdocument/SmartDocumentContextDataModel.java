package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.Map;

import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class SmartDocumentContextDataModel extends ContextInstance
    implements ISmartDocumentContextDataModel {
  
  private static final long                                       serialVersionUID = 1L;
  protected String                                                label;
  protected Map<String, ISmartDocumentTagInstanceDataModel>       tags;
  
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
  public Map<String, ISmartDocumentTagInstanceDataModel> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentTagInstanceDataModel.class)
  public void setTags(Map<String, ISmartDocumentTagInstanceDataModel> tags)
  {
    this.tags = tags;
  }
}
