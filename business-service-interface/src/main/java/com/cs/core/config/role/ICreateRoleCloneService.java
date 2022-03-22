package com.cs.core.config.role;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateRoleCloneModel;

public interface ICreateRoleCloneService
    extends ICreateConfigService<ICreateRoleCloneModel, ICreateOrSaveRoleResponseModel> {
  
}
