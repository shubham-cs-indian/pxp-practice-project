package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGlobalPermissionModel extends IModel {
  
  public static final String KLASS_IDS                     = "klassIds";
  public static final String TAXONOMY_IDS                  = "taxonomyIds";
  public static final String LOGIN_USER_ID                 = "loginUserId";
  public static final String ROLE_IDS_CONTAININGLOGIN_USER = "roleIdsContainingLoginUser";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getLoginUserId();
  
  public void setLoginUserId(String loginUserId);
  
  public List<String> getRoleIdsContainingLoginUser();
  
  public void setRoleIdsContainingLoginUser(List<String> roleIdsContainingLoginUser);
}
