package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;

public interface IGetRolesOrUsersDataByPartnerIdModel extends IGetConfigDataRequestModel {
  
  public static final String ORGANIZATION_ID = "organizationId";
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
}
