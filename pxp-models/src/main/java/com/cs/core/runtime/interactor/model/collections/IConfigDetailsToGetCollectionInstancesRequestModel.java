package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsToGetCollectionInstancesRequestModel extends IModel {
  
  public static final String CURRENT_USER_ID        = "currentUserId";
  public static final String SUB_PROMOTION_KLASS_ID = "subPromotionKlassId";
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public String getSubPromotionKlassId();
  
  public void setSubPromotionKlassId(String subPromotionKlassId);
}
