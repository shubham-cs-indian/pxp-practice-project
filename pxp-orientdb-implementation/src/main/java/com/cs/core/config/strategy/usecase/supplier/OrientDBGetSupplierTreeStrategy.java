package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassTreeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getSupplierTreeStrategy")
public class OrientDBGetSupplierTreeStrategy extends OrientDBBaseStrategy
    implements IGetKlassTreeStrategy {
  
  public static final String useCase = "GetSupplierTree";
  
  @Override
  public IConfigEntityTreeInformationModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, ConfigEntityTreeInformationModel.class);
  }
}
