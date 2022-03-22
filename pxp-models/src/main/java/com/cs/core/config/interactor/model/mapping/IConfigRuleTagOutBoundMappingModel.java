package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IConfigRuleTagOutBoundMappingModel extends IConfigRulePropertyMappingModel {
  
  public static final String TAG_VALUE_MAPPINGS = "tagValueMappings";
  
  public void setTagValueMappings(List<IColumnValueTagValueMappingModel> tagValueMappings);
  
  public List<IColumnValueTagValueMappingModel> getTagValueMappings();
}
