package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ValueIdTagModel extends ValueIdModel implements IValueIdTagModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected List<IIdRelevance> value            = new ArrayList<>();
  
  @Override
  public List<IIdRelevance> getValue()
  {
    return value;
  }
  
  @JsonDeserialize(contentAs = IdRelevance.class)
  @Override
  public void setValue(List<IIdRelevance> value)
  {
    this.value = value;
  }
}
