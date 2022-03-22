package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TagIdValueModel implements ITagIdValueModel {
  
  private static final long    serialVersionUID = 1L;
  protected String             tagId;
  protected List<IIdRelevance> tagValues;
  
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
  public List<IIdRelevance> getTagValues()
  {
    if (tagValues == null) {
      tagValues = new ArrayList<>();
    }
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdRelevance.class)
  public void setTagValues(List<IIdRelevance> tagValues)
  {
    this.tagValues = tagValues;
  }
}
