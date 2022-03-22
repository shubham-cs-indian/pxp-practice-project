package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISavePropertyCollectionModel extends IPropertyCollection, IModel {
  
  public static final String ADDED_ELEMENTS             = "addedElements";
  public static final String DELETED_ELEMENTS           = "deletedElements";
  public static final String MODIFIED_ELEMENTS          = "modifiedElements";
  public static final String ADDED_COMPLEX_ATTRIBUTE_ID = "addedComplexAttributeId";
  public static final String ADDED_TAB                  = "addedTab";
  public static final String DELETED_TAB                = "deletedTab";
  
  public List<IPropertyCollectionElement> getAddedElements();
  
  public void setAddedElements(List<IPropertyCollectionElement> propertyCollectionElement);
  
  public List<IPropertyCollectionElement> getModifiedElements();
  
  public void setModifiedElements(List<IPropertyCollectionElement> propertyCollectionElement);
  
  public List<IPropertyCollectionElement> getDeletedElements();
  
  public void setDeletedElements(List<IPropertyCollectionElement> propertyCollectionElement);
  
  public String getAddedComplexAttributeId();
  
  public void setAddedComplexAttributeId(String addedComplexAttributeId);
  
  public IAddedTabModel getAddedTab();
  
  public void setAddedTab(IAddedTabModel addedTab);
  
  public String getDeletedTab();
  
  public void setDeletedTab(String deletedTab);
}
