package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConfigRuleTagValueMappingModel extends IModel {
  
  public static final String TAG_VALUE_MAPPING = "tagValueMapping";
  
  public List<IColumnValueTagValueMappingModel> getTagValueMapping();
  
  public void setTagValueMapping(List<IColumnValueTagValueMappingModel> tagValueMapping);
}
