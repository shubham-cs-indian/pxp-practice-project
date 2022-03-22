package com.cs.core.config.strategy.usecase.klass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForTimelineTabStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class GetConfigDetailsForTimelineTabStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForTimelineTabStrategy {
  
  @Override
  @SuppressWarnings("unchecked")
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    Map<String, Object> map = ObjectMapperUtil.convertValue(model, HashMap.class);
    return execute(GET_CONFIG_DETAILS_FOR_TIMELINE_TAB, map,
        GetConfigDetailsForCustomTabModel.class);
  }
}
