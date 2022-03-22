package com.cs.core.runtime.interactor.model.tabledata;

import com.cs.core.runtime.interactor.model.configdetails.IMatchValueModel;
import com.cs.core.runtime.interactor.model.configdetails.MatchValueModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableAttributeModel implements ITableAttributeModel {
  
  protected String                 id;
  protected String                 attributeId;
  protected Map<String, String>    klassInstanceValues;
  protected List<IMatchValueModel> matchValues;
  
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
  public Map<String, String> getKlassInstanceValues()
  {
    return klassInstanceValues;
  }
  
  @Override
  public void setKlassInstanceValues(Map<String, String> klassInstanceValues)
  {
    this.klassInstanceValues = klassInstanceValues;
  }
  
  @Override
  public List<IMatchValueModel> getMatchValues()
  {
    if (matchValues == null) {
      matchValues = new ArrayList<IMatchValueModel>();
    }
    return matchValues;
  }
  
  @JsonDeserialize(contentAs = MatchValueModel.class)
  @Override
  public void setMatchValues(List<IMatchValueModel> attributeMatchValueModels)
  {
    this.matchValues = attributeMatchValueModels;
  }
}
