package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForBulkPropagationStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetConfigDetailsForBulkPropagationStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForBulkPropagationStrategy {
  
  @Override
  @SuppressWarnings("unchecked")
  public IConfigDetailsForBulkPropagationResponseModel execute(
      IMulticlassificationRequestModel model) throws Exception
  {
    Map<String, Object> map = ObjectMapperUtil.convertValue(model, HashMap.class);
    return execute(GET_CONFIG_DETAILS_FOR_BULK_PROPAGATION, map,
        ConfigDetailsForBulkPropagationResponseModel.class);
  }
}
