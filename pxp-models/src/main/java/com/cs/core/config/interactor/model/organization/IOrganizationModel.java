package com.cs.core.config.interactor.model.organization;

import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IOrganizationModel extends IConfigModel, IOrganization {
  
  public static final String ORIGINAL_INSTANCE_ID  = "originalInstanceId";
  public static final String IS_ONBOARDING_REQUEST = "isOnboardingRequest";
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public Boolean getIsOnboardingRequest();
  
  public void setIsOnboardingRequest(Boolean isOnboardingRequest);
}
