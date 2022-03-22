package com.cs.core.runtime.interactor.model.context;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.cs.core.config.interactor.model.context.IContextualPropertiesToInheritTagModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ContextualPropertiesToInheritTagModel
    extends AbstractContextualPropertiesToInheritPropertyModel
    implements IContextualPropertiesToInheritTagModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected List<IIdRelevance> tagValues;
  protected String             tagId;
  
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
}
