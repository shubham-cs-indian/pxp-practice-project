package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IGetGlobalPermissionForMultipleNatureTypesRequestModel extends IModel {
  
  public static final String KLASS_IDS = "klassIds";
  public static final String USER_ID   = "userId";
  
  public Set<String> getKlassIds();
  
  public void setKlassIds(Set<String> klassIds);
  
  public String getUserId();
  
  public void setUserId(String userId);
}
