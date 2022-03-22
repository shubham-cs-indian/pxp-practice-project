package com.cs.core.config.strategy.usecase.standard.properties;

import com.cs.core.config.interactor.entity.datarule.IMandatoryProperty;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAllStandardPropertiesStrategy extends OrientDBBaseStrategy
    implements IGetAllStandardPropertiesStrategy {
  
  public static final String useCase = "GetStandardProperties";
  
  @Override
  public IListModel<IMandatoryProperty> execute(IConfigModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(useCase, requestMap, new TypeReference<ListModel<IMandatoryProperty>>()
    {
      
    });
  }
}
