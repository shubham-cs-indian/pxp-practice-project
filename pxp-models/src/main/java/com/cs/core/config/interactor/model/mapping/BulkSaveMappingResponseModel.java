package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveMappingResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveMappingResponseModel {

  private static final long serialVersionUID = 1L;
  protected List<IMappingModel> mappings;
  
  @Override
  public List<IMappingModel> getmappings()
  {
    return mappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = MappingModel.class)
  public void setmappings(List<IMappingModel> mappings)
  {
    this.mappings = mappings;
  }
  
}
