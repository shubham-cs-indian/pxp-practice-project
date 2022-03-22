package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassInstanceVersionTypeModel extends KlassInstanceTypeModel
    implements IKlassInstanceVersionTypeModel {
  
  private static final long  serialVersionUID = 1L;
  protected String           parentId;
  protected IContextInstance context;
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public IContextInstance getContext()
  {
    return context;
  }
  
  @Override
  @JsonDeserialize(as = ContextInstance.class)
  public void setContext(IContextInstance context)
  {
    this.context = context;
  }
}
