package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ConfigRuleTagValueMappingModel implements IConfigRuleTagValueMappingModel {
  
  private static final long                        serialVersionUID = 1L;
  protected List<IColumnValueTagValueMappingModel> tagValueMapping;
  
  @Override
  public List<IColumnValueTagValueMappingModel> getTagValueMapping()
  {
    
    return tagValueMapping;
  }
  
  @Override
  @JsonDeserialize(contentAs = ColumnValueTagValueMappingModel.class)
  public void setTagValueMapping(List<IColumnValueTagValueMappingModel> tagValueMapping)
  {
    this.tagValueMapping = tagValueMapping;
  }
}
