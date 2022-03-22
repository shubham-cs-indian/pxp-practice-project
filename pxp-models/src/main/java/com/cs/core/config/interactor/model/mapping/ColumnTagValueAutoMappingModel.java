package com.cs.core.config.interactor.model.mapping;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ColumnTagValueAutoMappingModel extends ColumnValueTagValueMappingModel
    implements IColumnTagValueAutoMappingModel {
  
  private static final long serialVersionUID = 1L;
  protected ITag            tag;
  
  @Override
  public ITag getTag()
  {
    return tag;
  }
  
  @Override
  @JsonDeserialize(as = Tag.class)
  public void setTag(ITag tag)
  {
    this.tag = tag;
  }
}
