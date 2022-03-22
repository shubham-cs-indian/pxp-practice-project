package com.cs.core.config.interactor.model.mapping;

import com.cs.core.config.interactor.entity.tag.ITag;

public interface IColumnTagValueAutoMappingModel extends IColumnValueTagValueMappingModel {
  
  public static final String TAG = "tag";
  
  public ITag getTag();
  
  public void setTag(ITag tag);
}
