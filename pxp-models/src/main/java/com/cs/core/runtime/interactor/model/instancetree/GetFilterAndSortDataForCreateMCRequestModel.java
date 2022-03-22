package com.cs.core.runtime.interactor.model.instancetree;

public class GetFilterAndSortDataForCreateMCRequestModel extends GetNewFilterAndSortDataRequestModel
    implements IGetFilterAndSortDataForCreateMCRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          klassId;
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
}
