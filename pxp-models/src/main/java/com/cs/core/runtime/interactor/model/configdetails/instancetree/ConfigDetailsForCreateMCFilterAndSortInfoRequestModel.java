package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public class ConfigDetailsForCreateMCFilterAndSortInfoRequestModel extends ConfigDetailsForFilterAndSortInfoRequestModel 
  implements IConfigDetailsForCreateMCFilterAndSortInfoRequestModel{

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
