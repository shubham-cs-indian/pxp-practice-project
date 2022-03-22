package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.configdetails.IMatchValueModel;

import java.util.List;

public interface IAttributeMatchColumnModel {
  
  public static final String ATTRIBUTE_ID = "attribtueId";
  public static final String MATCH_VALUES = "matchValues";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public List<IMatchValueModel> getMatchValues();
  
  public void setMatchValues(List<IMatchValueModel> matchValues);
}
