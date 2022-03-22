package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.runtime.interactor.model.configdetails.IMatchValueModel;
import com.cs.core.runtime.interactor.model.configdetails.MatchValueModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TagMatchValueModel implements ITagMatchValueModel {
  
  protected List<IMatchValueModel> matchValues;
  protected String                 tagId;
  
  @Override
  public String getTagId()
  {
    return tagId;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
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
  public void setMatchValues(List<IMatchValueModel> matchValues)
  {
    this.matchValues = matchValues;
  }
}
