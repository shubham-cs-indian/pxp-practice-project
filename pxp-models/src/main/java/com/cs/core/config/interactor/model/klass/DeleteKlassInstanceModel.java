package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteKlassInstanceModel implements IDeleteKlassInstanceModel {
  
  protected String       baseType;
  protected List<String> ids;
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
}
