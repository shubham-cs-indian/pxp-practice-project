package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;

public interface IKlassInstanceVersionTypeModel extends IKlassInstanceTypeModel {
  
  String PARENT_ID = "parentId";
  String CONTEXT   = "context";
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public IContextInstance getContext();
  
  public void setContext(IContextInstance context);
}
