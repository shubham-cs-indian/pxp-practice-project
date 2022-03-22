package com.cs.core.config.role;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IRoleModel;

public interface IGetAllRolesService extends IGetConfigService<IRoleModel, IListModel<IRoleModel>> {
  
}
