package com.cs.core.runtime.interactor.model.component.klassinstance;

import java.util.Date;

public class SuppliersDiModel implements ISuppliersDiModel {
  
  private static final long           serialVersionUID = 1L;
  
  private Date                        timestamp;
  private IPropertyCollectionsDiModel supplierInfo;
  
  @Override
  public Date getTimestamp()
  {
    return timestamp;
  }
  
  @Override
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
  
  @Override
  public IPropertyCollectionsDiModel getSupplierInfo()
  {
    if (this.supplierInfo == null) {
      this.supplierInfo = new PropertyCollectionsDiModel();
    }
    return this.supplierInfo;
  }
  
  @Override
  public void setSupplierInfo(IPropertyCollectionsDiModel supplierInfo)
  {
    this.supplierInfo = supplierInfo;
  }
}
