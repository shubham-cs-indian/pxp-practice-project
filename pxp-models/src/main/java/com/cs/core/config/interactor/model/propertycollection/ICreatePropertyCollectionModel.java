package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ICreatePropertyCollectionModel extends IPropertyCollection, IConfigModel {
  
  public static final String TAB = "tab";
  
  public IAddedTabModel getTab();
  
  public void setTab(IAddedTabModel tab);
}
