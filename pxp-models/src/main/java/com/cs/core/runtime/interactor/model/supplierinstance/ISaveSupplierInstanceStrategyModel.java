package com.cs.core.runtime.interactor.model.supplierinstance;

import com.cs.core.config.interactor.entity.supplier.ISupplier;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveSupplierInstanceStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE = "klassInstance";
  public static final String TYPE_KLASS     = "typeKlass";
  
  public ISupplierInstanceSaveModel getKlassInstance();
  
  public void setKlassInstance(ISupplierInstanceSaveModel klassInstance);
  
  public ISupplier getTypeKlass();
  
  public void setTypeKlass(ISupplier typeKlass);
}
