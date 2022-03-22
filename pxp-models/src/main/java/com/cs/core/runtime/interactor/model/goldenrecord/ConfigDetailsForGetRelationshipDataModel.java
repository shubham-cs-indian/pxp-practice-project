package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigDetailsForGetRelationshipDataModel
    implements IConfigDetailsForGetRelationshipDataModel {
  
  private static final long   serialVersionUID = 1L;
  protected Map<String, ITag> referencedTags;
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
}
