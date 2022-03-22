package com.cs.core.runtime.interactor.model.filter;

import java.util.List;

public abstract class AbstractPropertyInstanceFilterModel<T extends IFilterValueModel>
    implements IPropertyInstanceFilterModel<T> {
  
  protected String  id;
  
  protected String  type;
  
  protected List<T> mandatory;
  
  protected List<T> should;
  
  protected boolean advancedSearchFilter;
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String instanceId)
  {
    this.id = instanceId;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public boolean getAdvancedSearchFilter()
  {
    return advancedSearchFilter;
  }
  
  @Override
  public void setAdvancedSearchFilter(boolean advancedSearchFilter)
  {
    this.advancedSearchFilter = advancedSearchFilter;
  }
}
