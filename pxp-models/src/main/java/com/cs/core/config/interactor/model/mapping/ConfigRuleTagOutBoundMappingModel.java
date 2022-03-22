package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigRuleTagOutBoundMappingModel extends ConfigRulePropertyMappingModel
    implements IConfigRuleTagOutBoundMappingModel {
  
  private static final long                        serialVersionUID = 1L;
  protected List<IColumnValueTagValueMappingModel> tagValueMappings = new ArrayList<>();
  
  @Override
  @JsonDeserialize(contentAs = ColumnValueTagValueMappingModel.class)
  public void setTagValueMappings(List<IColumnValueTagValueMappingModel> tagValueMappings)
  {
    this.tagValueMappings = tagValueMappings;
  }
  
  @Override
  public List<IColumnValueTagValueMappingModel> getTagValueMappings()
  {
    
    return tagValueMappings;
  }
  
}
