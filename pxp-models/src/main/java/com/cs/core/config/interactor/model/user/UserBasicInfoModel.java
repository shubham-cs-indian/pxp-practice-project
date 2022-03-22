package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class UserBasicInfoModel extends ConfigEntityInformationModel
    implements IUserBasicInfoModel {

  private static final long serialVersionUID = 1L;
  private String            userName;
  
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
  
  
  
}
