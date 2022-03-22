package com.cs.core.config.interactor.usecase.role;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.role.IGetRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRole extends IGetConfigInteractor<IIdParameterModel, IGetRoleModel> {
  
}
