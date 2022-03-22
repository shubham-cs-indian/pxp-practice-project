package com.cs.core.runtime.interactor.model.supplierinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.supplierinstance.ISupplierInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;

public interface ISupplierInstanceModel extends IRuntimeModel, ISupplierInstance {
  
  public static final String TYPE_KLASS = "typeKlass";
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
}
