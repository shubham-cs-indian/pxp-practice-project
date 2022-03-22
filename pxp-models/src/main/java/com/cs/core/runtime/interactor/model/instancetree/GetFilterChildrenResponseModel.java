package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

public class GetFilterChildrenResponseModel implements IGetFilterChildrenResponseModel {
  
  protected List<IGetFilterChildrenModel> filterChildren;
  protected IReferencedPropertyModel              referencedProperty;
  
  @Override
  public List<IGetFilterChildrenModel> getFilterChildren()
  {
    return filterChildren;
  }
  
  @Override
  public void setFilterChildren(List<IGetFilterChildrenModel> filterChildren)
  {
    this.filterChildren = filterChildren;
  }
  
  @Override
  public IReferencedPropertyModel getReferencedProperty()
  {
    return referencedProperty;
  }
  
  @Override
  public void setReferencedProperty(IReferencedPropertyModel referencedProperty)
  {
    this.referencedProperty = referencedProperty;
  }
}
