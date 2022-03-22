package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.configdetails.IMatchValueModel;
import com.cs.core.runtime.interactor.model.configdetails.MatchValueModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class AttributeMatchColumnModel implements IAttributeMatchColumnModel {
  
  protected String                 attributeId;
  protected List<IMatchValueModel> matchValues;
  
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
  public List<IMatchValueModel> getMatchValues()
  {
    return matchValues;
  }
  
  @JsonDeserialize(contentAs = MatchValueModel.class)
  @Override
  public void setMatchValues(List<IMatchValueModel> matchValues)
  {
    this.matchValues = matchValues;
  }
}
