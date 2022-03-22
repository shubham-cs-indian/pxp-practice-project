package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllRolesStrategy extends IConfigStrategy<IRoleModel, IListModel<IRoleModel>> {
  
}
