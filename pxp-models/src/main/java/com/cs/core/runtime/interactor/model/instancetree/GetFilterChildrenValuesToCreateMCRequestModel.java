package com.cs.core.runtime.interactor.model.instancetree;

public class GetFilterChildrenValuesToCreateMCRequestModel extends GetFilterChildrenRequestModel
    implements IGetFilterChildrenValuesToCreateMCRequestModel {
  
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
