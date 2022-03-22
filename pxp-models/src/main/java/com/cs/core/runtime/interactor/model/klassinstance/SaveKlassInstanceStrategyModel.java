package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;

public class SaveKlassInstanceStrategyModel implements ISaveKlassInstanceStrategyModel {
  
  @SuppressWarnings("unused")
  private static final long         serialVersionUID = 1L;
  
  protected IKlassInstanceSaveModel klassInstance;
  protected IKlass                  typeKlass;
  
  @Override
  public IKlassInstanceSaveModel getKlassInstance()
  {
    return klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstanceSaveModel assetInstance)
  {
    this.klassInstance = assetInstance;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
