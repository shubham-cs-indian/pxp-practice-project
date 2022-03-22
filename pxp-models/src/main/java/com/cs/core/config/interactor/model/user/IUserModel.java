package com.cs.core.config.interactor.model.user;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IUserModel extends IConfigModel, IUser, IConfigResponseWithAuditLogModel {
  
  public static final String ROLE_ID                 = "roleId";
  public static final String ORGANIZATION_NAME       = "organizationName";
  public static final String ORGANIZATION_TYPE       = "organizationType";
  public static final String PREFERRED_DATA_LANGUAGE = "preferredDataLanguage";
  public static final String PREFERRED_UI_LANGUAGE   = "preferredUILanguage";
  
  public String getRoleId();
  public void setRoleId(String roleId);
  
  public String getOrganizationName();
  public void setOrganizationName(String organizationName);
  
  public String getOrganizationType();
  public void setOrganizationType(String organizationType);
  
  public String getPreferredDataLanguage();
  public void setPreferredDataLanguage(String preferredDataLanguage);
  
  public String getPreferredUILanguage();
  public void setPreferredUILanguage(String preferredUILanguage);
}
