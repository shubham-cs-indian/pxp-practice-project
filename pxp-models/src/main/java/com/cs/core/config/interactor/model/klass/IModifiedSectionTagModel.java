package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;

import java.util.List;

public interface IModifiedSectionTagModel extends IModifiedSectionElementModel {
  
  public static final String TAG_TYPE                    = "tagType";
  public static final String IS_MULTI_SELECT             = "isMultiselect";
  public static final String ADDED_DEFAULT_VALUES        = "addedDefaultValues";
  public static final String MODIFIED_DEFAULT_VALUES     = "modifiedDefaultValues";
  public static final String DELETED_DEFAULT_VALUES      = "deletedDefaultValues";
  public static final String ADDED_SELECTED_TAG_VALUES   = "addedSelectedTagValues";
  public static final String DELETED_SELECTED_TAG_VALUES = "deletedSelectedTagValues";
  public static final String IS_VERSIONABLE              = "isVersionable";
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
  
  public String getTagType();
  
  public void setTagType(String tagType);
  
  public List<IIdRelevance> getAddedDefaultValues();
  
  public void setAddedDefaultValues(List<IIdRelevance> addedDefaultValues);
  
  public List<IIdRelevance> getModifiedDefaultValues();
  
  public void setModifiedDefaultValues(List<IIdRelevance> modifiedDefaultValues);
  
  public List<String> getDeletedDefaultValues();
  
  public void setDeletedDefaultValues(List<String> deletedDefaultValues);
  
  public List<String> getAddedSelectedTagValues();
  
  public void setAddedSelectedTagValues(List<String> addedSelectedTagValues);
  
  public List<String> getDeletedSelectedTagValues();
  
  public void setDeletedSelectedTagValues(List<String> deletedSelectedTagValues);
  
  public Boolean getIsVersionable();
  
  public void setIsVersionable(Boolean isVersionable);
}
