package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveKlassInstanceStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE = "klassInstance";
  public static final String TYPE_KLASS     = "typeKlass";
  
  public IKlassInstanceSaveModel getKlassInstance();
  
  public void setKlassInstance(IKlassInstanceSaveModel klassInstanceSaveModel);
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
}
