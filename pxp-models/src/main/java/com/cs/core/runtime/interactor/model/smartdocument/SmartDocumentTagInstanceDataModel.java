package com.cs.core.runtime.interactor.model.smartdocument;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class SmartDocumentTagInstanceDataModel implements ISmartDocumentTagInstanceDataModel {
  
  private static final long                              serialVersionUID = 1L;
  protected String                                       tagGroupId;
  protected String                                       tagGroupLabel;
  protected Map<String, ISmartDocumentTagValueDataModel> tagValues;
  protected Boolean                                      isMultiselect;
  
  @Override
  public String getTagGroupId()
  {
    return tagGroupId;
  }
  
  @Override
  public void setTagGroupId(String tagGroupId)
  {
    this.tagGroupId = tagGroupId;
  }
  
  @Override
  public String getTagGroupLabel()
  {
    return tagGroupLabel;
  }
  
  @Override
  public void setTagGroupLabel(String tagGroupLabel)
  {
    this.tagGroupLabel = tagGroupLabel;
  }
  
  @Override
  public Map<String, ISmartDocumentTagValueDataModel> getTagValues()
  {
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentTagValueDataModel.class)
  public void setTagValues(Map<String, ISmartDocumentTagValueDataModel> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
}
