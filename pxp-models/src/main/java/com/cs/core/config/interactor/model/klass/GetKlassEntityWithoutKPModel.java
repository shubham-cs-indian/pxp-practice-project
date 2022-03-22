package com.cs.core.config.interactor.model.klass;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassEntityWithoutKPModel extends ConfigResponseWithAuditLogModel
    implements IGetKlassEntityWithoutKPModel {
  
  private static final long                   serialVersionUID = 1L;
  protected IKlass                            entity;
  protected IGetKlassEntityConfigDetailsModel configDetails;
  
  @Override
  public IKlass getEntity()
  {
    return this.entity;
  }
  
  @Override
  public void setEntity(IKlass entity)
  {
    this.entity = entity;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public IGetKlassEntityConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetKlassEntityConfigDetailsModel.class)
  @Override
  public void setConfigDetails(IGetKlassEntityConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
