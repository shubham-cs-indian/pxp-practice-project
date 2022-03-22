package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITagDiffModel extends IModel {
  
  public static final String ID              = "id";
  public static final String OLD_VALUES      = "oldValues";
  public static final String ADDED_VALUES    = "addedValues";
  public static final String DELETED_VALUES  = "deletedValues";
  public static final String MODIFIED_VALUES = "modifiedValues";
  public static final String TAG             = "tag";
  public static final String SECTION_ELEMENT = "sectionElement";
  public static final String IS_ADDED        = "isAdded";
  
  public String getId();
  
  public void setId(String id);
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  /*  public void setMappingId(String mappingId);
  public String getMappingId();*/
  
  public List<ITagDiffValueModel> getOldValues();
  
  public void setOldValues(List<ITagDiffValueModel> oldValues);
  
  public List<ITagDiffValueModel> getAddedValues();
  
  public void setAddedValues(List<ITagDiffValueModel> addedValues);
  
  public List<ITagDiffValueModel> getDeletedValues();
  
  public void setDeletedValues(List<ITagDiffValueModel> deletedValues);
  
  public List<ITagDiffValueModel> getModifiedValues();
  
  public void setModifiedValues(List<ITagDiffValueModel> modifiedValues);
  
  public ISectionElement getSectionElement();
  
  public void setSectionElement(ISectionElement tag);
  
  public Boolean getIsAdded();
  
  public void setIsAdded(Boolean isAdded);
}
