package com.cs.core.runtime.interactor.model.collections;

public class ConfigDetailsToGetCollectionInstancesRequestModel
    implements IConfigDetailsToGetCollectionInstancesRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          currentUserId;
  protected String          subPromotionKlassId;
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public String getSubPromotionKlassId()
  {
    return subPromotionKlassId;
  }
  
  @Override
  public void setSubPromotionKlassId(String subPromotionKlassId)
  {
    this.subPromotionKlassId = subPromotionKlassId;
  }
}
