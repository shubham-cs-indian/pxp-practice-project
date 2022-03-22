package com.cs.di.config.model.authorization;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetPartnerAuthorizationModel extends ConfigResponseWithAuditLogModel implements IGetPartnerAuthorizationModel {
  
  private static final long                     serialVersionUID = 1L;
  protected IPartnerAuthorizationModel          entity;
  protected IConfigModelForPartnerAuthorization configDetails;
  
  @Override
  public IPartnerAuthorizationModel getEntity()
  {
    return entity;
  }
  
  @Override
  @JsonDeserialize(as = PartnerAuthorizationModel.class)
  public void setEntity(IPartnerAuthorizationModel entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IConfigModelForPartnerAuthorization getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ConfigModelForPartnerAuthorization.class)
  public void setConfigDetails(IConfigModelForPartnerAuthorization configDetails)
  {
    this.configDetails = configDetails;
  }
}
