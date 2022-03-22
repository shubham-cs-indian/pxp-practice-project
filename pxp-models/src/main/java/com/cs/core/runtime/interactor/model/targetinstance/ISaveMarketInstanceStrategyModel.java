package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveMarketInstanceStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE = "klassInstance";
  public static final String TYPE_KLASS     = "typeKlass";
  
  public IMarketInstanceSaveModel getKlassInstance();
  
  public void setKlassInstance(IMarketInstanceSaveModel klassInstance);
  
  public ITarget getTypeKlass();
  
  public void setTypeKlass(ITarget typeKlass);
}
