package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Date;

public interface ISuppliersDiModel extends IModel {
  
  public static final String TIMESTAMP     = "timestamp";
  public static final String SUPPLIER_INFO = "supplierInfo";
  
  public Date getTimestamp();
  
  public void setTimestamp(Date timestamp);
  
  public IPropertyCollectionsDiModel getSupplierInfo();
  
  public void setSupplierInfo(IPropertyCollectionsDiModel supplierInfo);
}
