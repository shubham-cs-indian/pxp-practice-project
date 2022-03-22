package com.cs.core.runtime.interactor.model.supplierinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.supplierinstance.ISupplierInstance;
import com.cs.core.runtime.interactor.entity.supplierinstance.SupplierInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceSaveModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public class SupplierInstanceSaveModel extends AbstractContentInstanceSaveModel
    implements ISupplierInstanceSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceSaveModel()
  {
    this.entity = new SupplierInstance();
  }
  
  public SupplierInstanceSaveModel(ISupplierInstance supplierInstance)
  {
    this.entity = supplierInstance;
  }
  
  @Override
  public String getBranchOf()
  {
    return ((ISupplierInstance) entity).getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    ((ISupplierInstance) entity).setBranchOf(branchOf);
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    // TODO Auto-generated method stub
    
  }
}
