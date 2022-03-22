package com.cs.core.runtime.interactor.model.usersession;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;

public class UserSessionModel implements IUserSessionModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          sessionId;
  protected String          userID;
  protected String          userName;
  protected String          userPassword;
  protected LogoutType      logoutType;
  
  @Override
  public String getSessionId()
  {
    return sessionId;
  }
  
  @Override
  public void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
  }
  
  @Override
  public LogoutType getLogoutType()
  {
    return logoutType;
  }
  
  @Override
  public void setLogoutType(LogoutType logoutType)
  {
    this.logoutType = logoutType;
  }
  
  @Override
  public String getUserID()
  {
    return userID;
  }
  
  @Override
  public void setUserID(String userID)
  {
    this.userID = userID;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public String getUserPassword()
  {
    return userPassword;
  }
  
  @Override
  public void setUserPassword(String userPassword)
  {
    this.userPassword = userPassword;
  }
}
