package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.propertycollection.AddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = CreateRelationshipModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class CreateRelationshipModel extends RelationshipModel implements ICreateRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  protected IAddedTabModel  tab;
  
  @Override
  public IAddedTabModel getTab()
  {
    return tab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setTab(IAddedTabModel tab)
  {
    this.tab = tab;
  }
}
