package com.cs.core.config.role;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;

public interface ICreateRoleService extends ICreateConfigService<ICreateRoleModel, ICreateOrSaveRoleResponseModel> {
  
}
