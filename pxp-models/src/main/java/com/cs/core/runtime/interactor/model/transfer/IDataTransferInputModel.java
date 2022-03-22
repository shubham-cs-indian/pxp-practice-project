package com.cs.core.runtime.interactor.model.transfer;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IDataTransferInputModel extends IModel {
  
  public static final String CONTENT_ID                          = "contentId";
  public static final String BASE_TYPE                           = "baseType";
  public static final String CHANGED_ATTRIBUTE_IDS               = "changedAttributeIds";
  public static final String CHANGED_TAGS_IDS                    = "changedTagsIds";
  public static final String MODIFIED_LANGUAGE_CODES             = "modifiedLanguageCodes";
  public static final String ADDED_ATTRIBUTE_IDS                 = "addedAttributeIds";
  public static final String ADDED_TAG_IDS                       = "addedTagIds";
  public static final String CHANGED_DEPENDENT_ATTRIBUTE_IDS_MAP = "changedDependentAttributeIdsMap";
  public static final String ADDED_DEPENDENT_ATTRIBUTE_IDS_MAP   = "addedDependentAttributeIdsMap";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getChangedAttributeIds();
  
  public void setChangedAttributeIds(List<String> changedAttributeIds);
  
  public List<String> getChangedTagsIds();
  
  public void setChangedTagsIds(List<String> changedTagsIds);
  
  public List<String> getModifiedLanguageCodes();
  
  public void setModifiedLanguageCodes(List<String> modifiedLanguageCodes);
  
  public List<String> getAddedAttributeIds();
  
  public void setAddedAttributeIds(List<String> addedAttributeIds);
  
  public List<String> getAddedTagIds();
  
  public void setAddedTagIds(List<String> addedTagIds);
  
  public Map<String, List<String>> getChangedDependentAttributeIdsMap();
  
  public void setChangedDependentAttributeIdsMap(
      Map<String, List<String>> changedDependentAttributeIdsMap);
  
  public Map<String, List<String>> getAddedDependentAttributeIdsMap();
  
  public void setAddedDependentAttributeIdsMap(
      Map<String, List<String>> addedDependentAttributeIdsMap);
}
