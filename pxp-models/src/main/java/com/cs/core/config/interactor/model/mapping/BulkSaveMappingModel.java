package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkSaveMappingModel implements IBulkSaveMappingModel {
  
  private static final long         serialVersionUID          = 1L;
  protected List<ISaveMappingModel> modifiedEndpointsMappings = new ArrayList<>();
  
  @Override
  public List<ISaveMappingModel> getModifiedMappings()
  {
    
    return modifiedEndpointsMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = SaveMappingModel.class)
  public void setModifiedMappings(List<ISaveMappingModel> modifiedEndpointsMappings)
  {
    this.modifiedEndpointsMappings = modifiedEndpointsMappings;
  }
}
