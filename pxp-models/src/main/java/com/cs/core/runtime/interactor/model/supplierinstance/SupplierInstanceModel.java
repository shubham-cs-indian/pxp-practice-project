package com.cs.core.runtime.interactor.model.supplierinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.supplier.Supplier;
import com.cs.core.runtime.interactor.entity.supplierinstance.ISupplierInstance;
import com.cs.core.runtime.interactor.entity.supplierinstance.SupplierInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class SupplierInstanceModel extends AbstractContentInstanceModel
    implements ISupplierInstanceModel {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  protected IKlass          typeKlass;
  
  public SupplierInstanceModel()
  {
    this.entity = new SupplierInstance();
  }
  
  public SupplierInstanceModel(ISupplierInstance supplierInstance)
  {
    this.entity = supplierInstance;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @JsonDeserialize(as = Supplier.class)
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
