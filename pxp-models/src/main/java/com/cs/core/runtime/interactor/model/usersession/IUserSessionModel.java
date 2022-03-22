package com.cs.core.runtime.interactor.model.usersession;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUserSessionModel extends IModel {
  
  public static final String USERID       = "userID";
  public static final String USERNAME     = "userName";
  public static final String USERPASSWORD = "userPassword";
  
  public LogoutType getLogoutType();
  
  public void setLogoutType(LogoutType logoutType);
  
  public String getSessionId();
  
  public void setSessionId(String sessionId);
  
  public String getUserID();
  
  public void setUserID(String userID);
  
  public String getUserName();
  
  public void setUserName(String userName);
  
  public String getUserPassword();
  
  public void setUserPassword(String userPassword);
}
