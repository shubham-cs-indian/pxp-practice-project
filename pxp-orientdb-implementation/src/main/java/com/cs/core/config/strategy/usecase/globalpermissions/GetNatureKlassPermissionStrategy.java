package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.INatureKlassWithPermissionrequestModel;
import com.cs.core.config.interactor.model.permission.GlobalPermissionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetNatureKlassPermissionStrategy extends OrientDBBaseStrategy
    implements IGetNatureKlassPermissionStrategy {
  
  @Override
  public IGlobalPermissionModel execute(INatureKlassWithPermissionrequestModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_NATURE_KLASS_PERMISSION, model,
        GlobalPermissionModel.class);
  }
}
