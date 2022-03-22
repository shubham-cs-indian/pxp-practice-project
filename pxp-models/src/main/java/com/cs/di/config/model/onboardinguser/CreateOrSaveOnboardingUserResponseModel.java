package com.cs.di.config.model.onboardinguser;

import java.util.List;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;


public class CreateOrSaveOnboardingUserResponseModel extends OnboardingUserModel
    implements ICreateOrSaveOnboardingUserResponseModel {


  private static final long serialVersionUID = 1L;
  protected List<IAuditLogModel> auditLogInfo; 
  
  @Override
  public List<IAuditLogModel> getAuditLogInfo()
  {
    return auditLogInfo;
  }

  @Override
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo)
  {
    this.auditLogInfo = auditLogInfo;
  }
  
}
