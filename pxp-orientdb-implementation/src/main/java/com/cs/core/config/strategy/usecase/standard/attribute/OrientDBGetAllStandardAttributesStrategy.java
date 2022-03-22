package com.cs.core.config.strategy.usecase.standard.attribute;

import com.cs.core.config.interactor.entity.standard.attribute.IStandardAttribute;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.attribute.IGetAllStandardAttributesStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAllStandardAttributesStrategy extends OrientDBBaseStrategy
    implements IGetAllStandardAttributesStrategy {
  
  public static final String useCase = "GetStandardAttributes";
  
  @Override
  public IListModel<IStandardAttribute> execute(IMandatoryAttributeModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(useCase, requestMap, new TypeReference<ListModel<IStandardAttribute>>()
    {
      
    });
  }
}
