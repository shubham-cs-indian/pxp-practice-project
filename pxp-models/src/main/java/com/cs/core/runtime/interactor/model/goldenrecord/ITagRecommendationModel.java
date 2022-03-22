package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;

import java.util.List;

public interface ITagRecommendationModel extends IRecommendationModel {
  
  public static final String TAG_VALUES = "tagValues";
  
  public List<IIdRelevance> getTagValues();
  
  public void setTagValues(List<IIdRelevance> defaultValue);
}
