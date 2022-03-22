package com.cs.core.config.role;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;

public interface ISaveRoleService extends ISaveConfigService<IRoleSaveModel, ICreateOrSaveRoleResponseModel> {
  
}
