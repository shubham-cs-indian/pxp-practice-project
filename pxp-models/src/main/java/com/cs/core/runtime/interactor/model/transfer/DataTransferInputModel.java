package com.cs.core.runtime.interactor.model.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTransferInputModel implements IDataTransferInputModel {
  
  private static final long           serialVersionUID = 1L;
  
  protected String                    contentId;
  protected String                    baseType;
  protected List<String>              changedAttributeIds;
  protected List<String>              changedTagsIds;
  protected List<String>              modifiedLanguageCodes;
  protected List<String>              addedAttributeIds;
  protected List<String>              addedTagIds;
  protected Map<String, List<String>> changedDependentAttributeIdsMap;
  protected Map<String, List<String>> addedDependentAttributeIdsMap;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getChangedAttributeIds()
  {
    if (changedAttributeIds == null) {
      changedAttributeIds = new ArrayList<>();
    }
    return changedAttributeIds;
  }
  
  @Override
  public void setChangedAttributeIds(List<String> changedAttributeIds)
  {
    this.changedAttributeIds = changedAttributeIds;
  }
  
  @Override
  public List<String> getChangedTagsIds()
  {
    if (changedTagsIds == null) {
      changedTagsIds = new ArrayList<>();
    }
    return changedTagsIds;
  }
  
  @Override
  public void setChangedTagsIds(List<String> changedTagsIds)
  {
    this.changedTagsIds = changedTagsIds;
  }
  
  @Override
  public List<String> getModifiedLanguageCodes()
  {
    if (modifiedLanguageCodes == null) {
      modifiedLanguageCodes = new ArrayList<>();
    }
    return modifiedLanguageCodes;
  }
  
  @Override
  public void setModifiedLanguageCodes(List<String> modifiedLanguageCodes)
  {
    this.modifiedLanguageCodes = modifiedLanguageCodes;
  }
  
  @Override
  public List<String> getAddedAttributeIds()
  {
    if (addedAttributeIds == null) {
      addedAttributeIds = new ArrayList<>();
    }
    return addedAttributeIds;
  }
  
  @Override
  public void setAddedAttributeIds(List<String> addedAttributeIds)
  {
    this.addedAttributeIds = addedAttributeIds;
  }
  
  @Override
  public List<String> getAddedTagIds()
  {
    if (addedTagIds == null) {
      addedTagIds = new ArrayList<>();
    }
    return addedTagIds;
  }
  
  @Override
  public void setAddedTagIds(List<String> addedTagIds)
  {
    this.addedTagIds = addedTagIds;
  }
  
  @Override
  public Map<String, List<String>> getChangedDependentAttributeIdsMap()
  {
    if (changedDependentAttributeIdsMap == null) {
      changedDependentAttributeIdsMap = new HashMap<>();
    }
    return changedDependentAttributeIdsMap;
  }
  
  @Override
  public void setChangedDependentAttributeIdsMap(
      Map<String, List<String>> changedDependentAttributeIdsMap)
  {
    this.changedDependentAttributeIdsMap = changedDependentAttributeIdsMap;
  }
  
  @Override
  public Map<String, List<String>> getAddedDependentAttributeIdsMap()
  {
    if (addedDependentAttributeIdsMap == null) {
      addedDependentAttributeIdsMap = new HashMap<>();
    }
    return addedDependentAttributeIdsMap;
  }
  
  @Override
  public void setAddedDependentAttributeIdsMap(
      Map<String, List<String>> addedDependentAttributeIdsMap)
  {
    this.addedDependentAttributeIdsMap = addedDependentAttributeIdsMap;
  }
}
