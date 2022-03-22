package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.klassinstance.IMarketInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;

public interface IMarketInstanceModel extends IRuntimeModel, IMarketInstance {
  
  public static final String TYPE_KLASS = "typeKlass";
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
}
