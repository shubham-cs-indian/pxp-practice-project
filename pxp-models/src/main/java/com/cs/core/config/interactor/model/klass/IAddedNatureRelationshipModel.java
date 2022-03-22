package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;

public interface IAddedNatureRelationshipModel extends IKlassNatureRelationship {
  
  public static final String ADDED_PROPERTY_COLLECTION = "addedPropertyCollection";
  public static final String TAB                       = "tab";
  
  public String getAddedPropertyCollection();
  
  public void setAddedPropertyCollection(String addedPropertyCollection);
  
  public IAddedTabModel getTab();
  
  public void setTab(IAddedTabModel tab);
}
