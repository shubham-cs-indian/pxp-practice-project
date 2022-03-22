package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBDeleteAttributesStrategy extends OrientDBBaseStrategy
    implements IDeleteAttributeStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return super.execute(OrientDBBaseStrategy.DELETE_ATTRIBUTE, model, BulkDeleteReturnModel.class);
  }
}
