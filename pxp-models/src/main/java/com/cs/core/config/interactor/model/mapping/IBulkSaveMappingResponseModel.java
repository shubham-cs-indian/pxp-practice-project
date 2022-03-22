package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;


public interface IBulkSaveMappingResponseModel extends IConfigResponseWithAuditLogModel {
  
  public static final String MAPPINGS = "mappings";
  
  public List<IMappingModel> getmappings();
  public void setmappings(List<IMappingModel> mappings);
  
}
