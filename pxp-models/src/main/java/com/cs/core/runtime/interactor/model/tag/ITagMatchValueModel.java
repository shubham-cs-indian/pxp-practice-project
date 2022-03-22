package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.runtime.interactor.model.configdetails.IMatchValueModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITagMatchValueModel extends IModel {
  
  public static final String TAG_ID       = "tagId";
  public static final String MATCH_VALUES = "matchValues";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public List<IMatchValueModel> getMatchValues();
  
  public void setMatchValues(List<IMatchValueModel> matchColumn);
}
