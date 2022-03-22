package com.cs.core.config.interactor.model.user;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IUserBasicInfoModel extends IConfigEntityInformationModel{
  
  public String getUserName();
  
  public void setUserName(String userName); 
}
