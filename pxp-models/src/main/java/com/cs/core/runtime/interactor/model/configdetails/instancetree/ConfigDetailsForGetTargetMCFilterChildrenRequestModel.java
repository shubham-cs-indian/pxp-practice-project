package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import com.cs.core.config.interactor.model.instancetree.ConfigDetailsForGetFilterChildrenRequestModel;

public class ConfigDetailsForGetTargetMCFilterChildrenRequestModel extends ConfigDetailsForGetFilterChildrenRequestModel
    implements IConfigDetailsForGetTargetMCFilterChildrenRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          klassId;
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
}
