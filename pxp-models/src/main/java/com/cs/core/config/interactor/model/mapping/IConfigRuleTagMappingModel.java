package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IConfigRuleTagMappingModel extends IConfigRuleAttributeMappingModel {
  
  public static final String TAG_VALUE_MAPPINGS = "tagValueMappings";
  
  public List<IColumnValueTagValueMappingModel> getTagValueMappings();
  
  public void setTagValueMappings(List<IColumnValueTagValueMappingModel> tagValueMappings);
}
