package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.taxonomy.IAppliedSortData;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

import java.util.List;

public interface IADMAppliedSortDataModel extends IAppliedSortData, IConfigModel {
  
  public static final String ADDED_ATTRIBUTES   = "addedAttributes";
  public static final String DELETED_ATTRIBUTES = "deletedAttributes";
  
  public List<String> getAddedAttributes();
  
  public void setAddedAttributes(List<String> addedAttributes);
  
  public List<String> getDeletedAttributes();
  
  public void setDeletedAttributes(List<String> deletedAttributes);
}
