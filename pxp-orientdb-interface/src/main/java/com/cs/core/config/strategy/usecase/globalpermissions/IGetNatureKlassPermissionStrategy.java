package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.INatureKlassWithPermissionrequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetNatureKlassPermissionStrategy
    extends IConfigStrategy<INatureKlassWithPermissionrequestModel, IGlobalPermissionModel> {
  
}
