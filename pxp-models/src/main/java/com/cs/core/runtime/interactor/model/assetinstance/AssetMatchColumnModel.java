package com.cs.core.runtime.interactor.model.assetinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class AssetMatchColumnModel implements IAssetMatchColumnModel {
  
  protected String                      attributeId;
  protected List<IMatchAssetValueModel> matchValues;
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public List<IMatchAssetValueModel> getMatchValues()
  {
    return matchValues;
  }
  
  @JsonDeserialize(contentAs = MatchAssetValueModel.class)
  @Override
  public void setMatchValues(List<IMatchAssetValueModel> matchValues)
  {
    this.matchValues = matchValues;
  }
}
