package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ConfigRuleTagMappingModel extends ConfigRuleAttributeMappingModel
    implements IConfigRuleTagMappingModel {
  
  private static final long                        serialVersionUID = 1L;
  protected List<IColumnValueTagValueMappingModel> tagValueMappings = new ArrayList<>();
  
  @Override
  public List<IColumnValueTagValueMappingModel> getTagValueMappings()
  {
    
    return tagValueMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ColumnValueTagValueMappingModel.class)
  public void setTagValueMappings(List<IColumnValueTagValueMappingModel> tagValueMappings)
  {
    this.tagValueMappings = tagValueMappings;
  }
}
