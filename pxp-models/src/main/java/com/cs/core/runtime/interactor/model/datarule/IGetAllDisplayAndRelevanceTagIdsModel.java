package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllDisplayAndRelevanceTagIdsModel extends IModel {
  
  public static final String DISPLAY_TAG_IDS   = "displayTagIds";
  public static final String RELEVANCE_TAG_IDS = "relevanceTagIds";
  
  public List<String> getDisplayTagIds();
  
  public void setDisplayTagIds(List<String> displayTagIds);
  
  public List<String> getRelevanceTagIds();
  
  public void setRelevanceTagIds(List<String> relevanceTagIds);
}
