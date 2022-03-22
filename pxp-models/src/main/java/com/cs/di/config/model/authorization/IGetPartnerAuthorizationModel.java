package com.cs.di.config.model.authorization;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;

public interface IGetPartnerAuthorizationModel extends IConfigResponseWithAuditLogModel {
  
  public static final String CONFIG_DETAILS = "configDetails";
  public static final String ENTITY         = "entity";
  
  public IPartnerAuthorizationModel getEntity();
  
  public void setEntity(IPartnerAuthorizationModel entity);
  
  public IConfigModelForPartnerAuthorization getConfigDetails();
  
  public void setConfigDetails(IConfigModelForPartnerAuthorization configDetails);
}
