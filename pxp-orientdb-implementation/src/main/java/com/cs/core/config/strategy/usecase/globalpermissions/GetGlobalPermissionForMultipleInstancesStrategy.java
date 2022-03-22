package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForMultipleEntityResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleEntityResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleInstancesRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGlobalPermissionForMultipleInstancesStrategy extends OrientDBBaseStrategy
    implements IGetGlobalPermissionForMultipleInstancesStrategy {
  
  @Override
  public IGetGlobalPermissionForMultipleEntityResponseModel execute(
      IGetGlobalPermissionForMultipleInstancesRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GLOBAL_PERMISSION_FOR_MULTIPLE_INSTANCES, model,
        GetGlobalPermissionForMultipleEntityResponseModel.class);
  }
}
