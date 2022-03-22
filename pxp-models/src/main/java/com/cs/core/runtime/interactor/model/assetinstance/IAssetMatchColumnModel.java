package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.List;

public interface IAssetMatchColumnModel {
  
  public static final String ATTRIBUTE_ID = "attributeId";
  public static final String MATCH_VALUES = "matchValues";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public List<IMatchAssetValueModel> getMatchValues();
  
  public void setMatchValues(List<IMatchAssetValueModel> matchColumn);
}
