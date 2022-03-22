package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.propertycollection.AddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveRelationshipsModel extends RelationshipInformationModel
    implements IBulkSaveRelationshipsModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IAddedTabModel  addedTab;
  protected String          deletedTab;
  
  @Override
  public String getDeletedTab()
  {
    return deletedTab;
  }
  
  @Override
  public void setDeletedTab(String deletedTab)
  {
    this.deletedTab = deletedTab;
  }
  
  @Override
  public IAddedTabModel getAddedTab()
  {
    return addedTab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setAddedTab(IAddedTabModel addedTab)
  {
    this.addedTab = addedTab;
  }
}
