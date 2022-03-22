package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetDefaultKlassesModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesForModulesModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetDefaultKlassesForModulesStrategy extends OrientDBBaseStrategy
    implements IGetDefaultKlassesForModulesStrategy {
  
  @Override
  public IGetDefaultKlassesModel execute(IListModel<IGetAllowedTypesForModulesModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getList());
    return execute(GET_DEFAULT_KLASSES_FOR_MODULES, requestMap, GetDefaultKlassesModel.class);
  }
}
