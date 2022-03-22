package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;

public interface IConfigDetailsForGetTargetMCFilterChildrenRequestModel extends IConfigDetailsForGetFilterChildrenRequestModel {
  
  public static final String KLASS_ID         = "klassId";
  
  public String getKlassId();
  public void setKlassId(String klassId);
}
