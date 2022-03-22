package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkSaveMappingModel extends IModel {
  
  public static final String MODIFIED_MAPPINGS = "modifiedMappings";
  
  public List<ISaveMappingModel> getModifiedMappings();
  
  public void setModifiedMappings(List<ISaveMappingModel> modifiedEndpointsMappings);
}
