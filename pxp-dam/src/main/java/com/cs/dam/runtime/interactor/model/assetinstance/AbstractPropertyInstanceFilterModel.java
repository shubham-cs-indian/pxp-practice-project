package com.cs.dam.runtime.interactor.model.assetinstance;

import java.util.List;

public abstract class AbstractPropertyInstanceFilterModel<T extends IFilterValueModel>
    implements IAssetPropertyInstanceFilterModel<T> {
  
  protected String  id;
  
  protected String  type;
  
  protected List<T> mandatory;
  
  protected List<T> should;
  
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
}
