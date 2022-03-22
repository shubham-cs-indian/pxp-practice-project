package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateRoleCloneModel;

public interface ICreateRoleClone extends ICreateConfigInteractor<ICreateRoleCloneModel, ICreateOrSaveRoleResponseModel> {
  
}
