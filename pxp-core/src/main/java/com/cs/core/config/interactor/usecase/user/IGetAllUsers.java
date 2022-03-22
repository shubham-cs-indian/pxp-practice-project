package com.cs.core.config.interactor.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.IUserModel;

public interface IGetAllUsers
    extends IConfigInteractor<IUserModel, IListModel<IUserInformationModel>> {
  
}
