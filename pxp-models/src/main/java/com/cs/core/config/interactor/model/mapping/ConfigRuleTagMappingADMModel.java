package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ConfigRuleTagMappingADMModel extends ConfigRuleAttributeMappingModel
    implements IConfigRuleTagMappingADMModel {
  
  private static final long             serialVersionUID         = 1L;
  protected List<ITagValueMappingModel> addedTagValueMappings    = new ArrayList<>();
  protected List<ITagValueMappingModel> modifiedTagValueMappings = new ArrayList<>();
  protected List<String>                deletedTagValueMappings  = new ArrayList<>();
  
  @Override
  public List<ITagValueMappingModel> getAddedTagValueMappings()
  {
    
    return addedTagValueMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagValueMappingModel.class)
  public void setAddedTagValueMappings(List<ITagValueMappingModel> addedTagValueMappings)
  {
    this.addedTagValueMappings = addedTagValueMappings;
  }
  
  @Override
  public List<ITagValueMappingModel> getModifiedTagValueMappings()
  {
    
    return modifiedTagValueMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagValueMappingModel.class)
  public void setModifiedTagValueMappings(List<ITagValueMappingModel> modifiedTagValueMappings)
  {
    this.modifiedTagValueMappings = modifiedTagValueMappings;
  }
  
  @Override
  public List<String> getDeletedTagValueMappings()
  {
    
    return deletedTagValueMappings;
  }
  
  @Override
  public void setDeletedTagValueMappings(List<String> deletedTagValueMappings)
  {
    this.deletedTagValueMappings = deletedTagValueMappings;
  }
}
