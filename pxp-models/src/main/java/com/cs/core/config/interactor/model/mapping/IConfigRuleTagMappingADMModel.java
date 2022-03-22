package com.cs.core.config.interactor.model.mapping;

import java.util.List;

public interface IConfigRuleTagMappingADMModel extends IConfigRuleAttributeMappingModel {
  
  public static final String ADDED_TAG_VALUE_MAPPINGS    = "addedTagValueMappings";
  public static final String MODIFIED_TAG_VALUE_MAPPINGS = "modifiedTagValueMappings";
  public static final String DELETED_TAG_VALUE_MAPPINGS  = "deletedTagValueMappings";
  
  public List<ITagValueMappingModel> getAddedTagValueMappings();
  
  public void setAddedTagValueMappings(List<ITagValueMappingModel> addedTagValueMappings);
  
  public List<ITagValueMappingModel> getModifiedTagValueMappings();
  
  public void setModifiedTagValueMappings(List<ITagValueMappingModel> modifiedTagValueMappings);
  
  public List<String> getDeletedTagValueMappings();
  
  public void setDeletedTagValueMappings(List<String> deletedTagValueMappings);
}
