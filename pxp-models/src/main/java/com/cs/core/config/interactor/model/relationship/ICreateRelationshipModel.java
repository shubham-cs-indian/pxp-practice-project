package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;

public interface ICreateRelationshipModel extends IRelationshipModel {
  
  public static final String TAB = "tab";
  
  public IAddedTabModel getTab();
  
  public void setTab(IAddedTabModel tab);
}
