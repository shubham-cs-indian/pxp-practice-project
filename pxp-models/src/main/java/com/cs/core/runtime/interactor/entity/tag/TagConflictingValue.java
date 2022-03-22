package com.cs.core.runtime.interactor.entity.tag;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.cs.core.runtime.interactor.entity.datarule.AbstractConflictingValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class TagConflictingValue extends AbstractConflictingValue implements ITagConflictingValue {
  
  private static final long    serialVersionUID = 1L;
  
  protected List<IIdRelevance> tagValues        = new ArrayList<>();
  
  @Override
  public List<IIdRelevance> getTagValues()
  {
    return tagValues;
  }
  
  @JsonDeserialize(contentAs = IdRelevance.class)
  @Override
  public void setTagValues(List<IIdRelevance> tagValues)
  {
    this.tagValues = tagValues;
  }
}
