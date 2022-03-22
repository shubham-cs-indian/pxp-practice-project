package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.GetGlobalPermissionForMultipleNatureTypesResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGlobalPermissionForMultipleNatureTypesStrategy extends OrientDBBaseStrategy
    implements IGetGlobalPermissionForMultipleNatureTypesStrategy {
  
  @Override
  public IGetGlobalPermissionForMultipleNatureTypesResponseModel execute(
      IGetGlobalPermissionForMultipleNatureTypesRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GLOBAL_PERMISSION_FOR_MULTIPLE_NATURE_TYPES, model,
        GetGlobalPermissionForMultipleNatureTypesResponseModel.class);
  }
}
