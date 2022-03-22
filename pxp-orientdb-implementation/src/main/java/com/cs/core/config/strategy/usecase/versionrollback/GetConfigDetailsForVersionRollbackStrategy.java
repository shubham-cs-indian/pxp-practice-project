package com.cs.core.config.strategy.usecase.versionrollback;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.usecase.versionrollback.IGetConfigDetailForVersionRollbackStrategy;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class GetConfigDetailsForVersionRollbackStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailForVersionRollbackStrategy {
  
  @Override
  @SuppressWarnings("unchecked")
  public IGetConfigDetailsForVersionRollbackModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    Map<String, Object> map = ObjectMapperUtil.convertValue(model, HashMap.class);
    return execute(GET_CONFIG_DETAILS_FOR_VERSION_ROLLBACK, map,
        GetConfigDetailsForVersionRollbackModel.class);
  }
}
