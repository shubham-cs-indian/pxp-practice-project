package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertiesOfPropertyCollectionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetPropertiesOfPropertyCollectionStrategy extends
    IConfigStrategy<IGetPropertiesOfPropertyCollectionModel, IGetGlobalPermissionsForRoleModel> {
  
}
