package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;

public interface IBulkSaveRelationshipsModel extends IRelationshipInformationModel {
  
  public static final String ADDED_TAB   = "addedTab";
  public static final String DELETED_TAB = "deletedTab";
  
  public IAddedTabModel getAddedTab();
  
  public void setAddedTab(IAddedTabModel addedTab);
  
  public String getDeletedTab();
  
  public void setDeletedTab(String deletedTab);
}
