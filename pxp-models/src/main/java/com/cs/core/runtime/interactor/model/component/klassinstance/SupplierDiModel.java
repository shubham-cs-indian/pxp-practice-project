package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SupplierDiModel implements ISupplierDiModel {
  
  private static final long       serialVersionUID = 1L;
  
  private Date                    timestamp;
  private List<ISuppliersDiModel> suppliers;
  
  @Override
  public Date getTimestamp()
  {
    return this.timestamp;
  }
  
  @Override
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
  
  @Override
  public List<ISuppliersDiModel> getSuppliers()
  {
    if (this.suppliers == null) {
      this.suppliers = new ArrayList<ISuppliersDiModel>();
    }
    return this.suppliers;
  }
  
  @Override
  @JsonDeserialize(contentAs = ISuppliersDiModel.class)
  public void setSuppliers(List<ISuppliersDiModel> suppliers)
  {
    this.suppliers = suppliers;
  }
}
