package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TagRecommendationModel extends AbstractRecommendationModel
    implements ITagRecommendationModel {
  
  private static final long    serialVersionUID = 1L;
  protected List<IIdRelevance> defaultValue;
  
  @Override
  public List<IIdRelevance> getTagValues()
  {
    if (defaultValue == null) {
      defaultValue = new ArrayList<>();
    }
    return defaultValue;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdRelevance.class)
  public void setTagValues(List<IIdRelevance> defaultValue)
  {
    this.defaultValue = defaultValue;
  }
}
