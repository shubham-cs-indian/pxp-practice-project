package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ISmartDocumentTagInstanceDataModel extends IModel {
  
  public static final String TAG_GROUP_ID    = "tagGroupId";
  public static final String TAG_GROUP_LABEL = "tagGroupLabel";
  public static final String TAG_VALUES      = "tagValues";
  public static final String IS_MULTISELECT  = "isMultiselect";
  
  public String getTagGroupId();
  
  public void setTagGroupId(String tagGroupId);
  
  public String getTagGroupLabel();
  
  public void setTagGroupLabel(String tagGroupLabel);
  
  public Map<String, ISmartDocumentTagValueDataModel> getTagValues();
  
  public void setTagValues(Map<String, ISmartDocumentTagValueDataModel> tagValues);
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
}
