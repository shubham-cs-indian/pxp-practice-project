package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.IGetAllUsersService;

@Service
public class GetAllUsers extends AbstractGetConfigInteractor<IUserModel, IListModel<IUserInformationModel>> implements IGetAllUsers {
  
  @Autowired
  IGetAllUsersService getAllUsersService;
  
  @Override
  public IListModel<IUserInformationModel> executeInternal(IUserModel dataModel) throws Exception
  {
    return getAllUsersService.execute(dataModel);
  }
}
