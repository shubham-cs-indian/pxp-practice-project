package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.taxonomy.IDefaultFilters;
import com.cs.core.config.interactor.entity.taxonomy.IFilterTagValue;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedDefaultFilterModel extends IDefaultFilters, IModel {
  
  public static final String ADDED_TAG_VALUES    = "addedTagValues";
  public static final String MODIFIED_TAG_VALUES = "modifiedTagValues";
  public static final String DELETED_TAG_VALUES  = "deletedTagValues";
  
  public List<IFilterTagValue> getAddedTagValues();
  
  public void setAddedTagValues(List<IFilterTagValue> tagValues);
  
  public List<IFilterTagValue> getModifiedTagValues();
  
  public void setModifiedTagValues(List<IFilterTagValue> tagValues);
  
  public List<String> getDeletedTagValues();
  
  public void setDeletedTagValues(List<String> tagValues);
}
