package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Date;
import java.util.List;

public interface ISupplierDiModel extends IModel {
  
  public static final String TIMESTAMP = "timestamp";
  public static final String SUPPLIERS = "suppliers";
  
  public Date getTimestamp();
  
  public void setTimestamp(Date timestamp);
  
  public List<ISuppliersDiModel> getSuppliers();
  
  public void setSuppliers(List<ISuppliersDiModel> suppliersv);
}
