package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.datarule.IGetAllDisplayAndRelevanceTagIdsModel;

import java.util.ArrayList;
import java.util.List;

public class GetAllDisplayAndRelavanceTagIds implements IGetAllDisplayAndRelevanceTagIdsModel {
  
  protected List<String> displayTagIds;
  protected List<String> relevanceTagIds;
  
  @Override
  public List<String> getDisplayTagIds()
  {
    if (displayTagIds == null) {
      displayTagIds = new ArrayList<String>();
    }
    return displayTagIds;
  }
  
  @Override
  public void setDisplayTagIds(List<String> displayTagIds)
  {
    this.displayTagIds = displayTagIds;
  }
  
  @Override
  public List<String> getRelevanceTagIds()
  {
    if (relevanceTagIds == null) {
      relevanceTagIds = new ArrayList<String>();
    }
    return relevanceTagIds;
  }
  
  @Override
  public void setRelevanceTagIds(List<String> relevanceTagIds)
  {
    this.relevanceTagIds = relevanceTagIds;
  }
}
