package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public class ConfigDetailsForGetTargetToCreateMCRequestModel extends GetConfigDetailsForGetNewInstanceTreeRequestModel 
  implements IConfigDetailsForGetTargetToCreateMCRequestModel {

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
