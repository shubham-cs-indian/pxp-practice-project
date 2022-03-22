package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.klass.GetKlassWithGlobalPermissionModel;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetTextAssetWithGlobalPermissionStrategy extends OrientDBBaseStrategy
    implements IGetTextAssetWithGlobalPermissionStrategy {
  
  public static final String useCase = "GetTextAssetWithGlobalPermission";
  
  @Override
  public IGetKlassWithGlobalPermissionModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, GetKlassWithGlobalPermissionModel.class);
  }
}
