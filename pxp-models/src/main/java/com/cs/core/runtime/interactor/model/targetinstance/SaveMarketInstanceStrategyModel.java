package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.config.interactor.entity.klass.ITarget;

public class SaveMarketInstanceStrategyModel implements ISaveMarketInstanceStrategyModel {
  
  protected IMarketInstanceSaveModel klassInstance;
  protected ITarget                  typeKlass;
  
  @Override
  public IMarketInstanceSaveModel getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(IMarketInstanceSaveModel klassInstance)
  {
    this.klassInstance = (IMarketInstanceSaveModel) klassInstance;
  }
  
  @Override
  public ITarget getTypeKlass()
  {
    return this.typeKlass;
  }
  
  @Override
  public void setTypeKlass(ITarget typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
