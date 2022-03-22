package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetFilterChildrenResponseModel extends IModel {
  
  String FILTER_CHILDREN     = "filterChildren";
  String REFERENCED_PROPERTY = "referencedProperty";
  
  public List<IGetFilterChildrenModel> getFilterChildren();
  public void setFilterChildren(List<IGetFilterChildrenModel> filterChildren);
  
  public IReferencedPropertyModel getReferencedProperty();
  public void setReferencedProperty(IReferencedPropertyModel referencedProperty);
}
