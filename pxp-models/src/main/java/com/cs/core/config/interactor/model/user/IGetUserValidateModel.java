package com.cs.core.config.interactor.model.user;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetUserValidateModel extends IModel {
  
  public static final String ID            = "id";
  public static final String USER_ID       = "userId";
  public static final String LABEL         = "label";
  public static final String USER_NAME     = "userName";
  public static final String IS_VALID_USER = "isValidUser";
  public static final String IDP           = "idp";
  public static final String TYPE          = "type";
  public static final String URL           = "url";
  
  public String getId();
  
  public void setId(String id);
  
  public String getIdp();
  
  public void setIdp(String idp);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getUserName();
  
  public void setUserName(String userName);
  
  public Boolean getIsValidUser();
  
  public void setIsValidUser(Boolean isValidUser);
  
  public String getType();
  
  public void setType(String type);
  
  public String getUrl();
  
  public void setUrl(String url);
}
