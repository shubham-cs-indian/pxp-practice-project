package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertyCollectionsForEntityModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetPropertyCollectionsForEntityStrategy extends
    IConfigStrategy<IGetPropertyCollectionsForEntityModel, IGetGlobalPermissionsForRoleModel> {
  
}
