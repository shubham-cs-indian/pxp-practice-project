package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetKlassInstancesToCompare;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetConfigDetailsForGetKlassInstancesToCompareStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetKlassInstancesToCompare {
  
  @Override
  @SuppressWarnings("unchecked")
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    Map<String, Object> map = ObjectMapperUtil.convertValue(model, HashMap.class);
    return execute(GET_CONFIG_DETAILS_FOR_GET_KLASS_INSTANCE_TO_COMPARE, map,
        GetConfigDetailsForCustomTabModel.class);
  }
}
