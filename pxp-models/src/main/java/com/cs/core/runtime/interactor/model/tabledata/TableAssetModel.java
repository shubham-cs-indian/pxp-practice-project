package com.cs.core.runtime.interactor.model.tabledata;

import com.cs.core.runtime.interactor.model.assetinstance.IMatchAssetValueModel;
import com.cs.core.runtime.interactor.model.assetinstance.MatchAssetValueModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableAssetModel implements ITableAssetModel {
  
  protected String                      id;
  protected String                      attributeId;
  protected Map<String, List<String>>   klassInstanceValues;
  protected List<IMatchAssetValueModel> matchValues;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
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
  public Map<String, List<String>> getKlassInstanceValues()
  {
    return klassInstanceValues;
  }
  
  @Override
  public void setKlassInstanceValues(Map<String, List<String>> klassInstanceValues)
  {
    this.klassInstanceValues = klassInstanceValues;
  }
  
  @Override
  public List<IMatchAssetValueModel> getMatchValues()
  {
    if (matchValues == null) {
      matchValues = new ArrayList<IMatchAssetValueModel>();
    }
    return matchValues;
  }
  
  @JsonDeserialize(contentAs = MatchAssetValueModel.class)
  @Override
  public void setMatchValues(List<IMatchAssetValueModel> attributeMatchValueModels)
  {
    this.matchValues = attributeMatchValueModels;
  }
}
