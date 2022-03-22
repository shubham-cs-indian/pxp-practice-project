package com.cs.core.runtime.interactor.model.supplierinstance;

import com.cs.core.config.interactor.entity.supplier.ISupplier;

public class SaveSupplierInstanceStrategyModel implements ISaveSupplierInstanceStrategyModel {
  
  protected ISupplierInstanceSaveModel klassInstance;
  protected ISupplier                  typeKlass;
  
  @Override
  public ISupplierInstanceSaveModel getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(ISupplierInstanceSaveModel klassInstance)
  {
    this.klassInstance = (ISupplierInstanceSaveModel) klassInstance;
  }
  
  @Override
  public ISupplier getTypeKlass()
  {
    return this.typeKlass;
  }
  
  @Override
  public void setTypeKlass(ISupplier typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
