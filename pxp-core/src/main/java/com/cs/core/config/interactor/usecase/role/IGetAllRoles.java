package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IRoleModel;

public interface IGetAllRoles extends IGetConfigInteractor<IRoleModel, IListModel<IRoleModel>> {
  
}
