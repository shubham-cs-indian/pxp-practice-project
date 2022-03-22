package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetOrCreateKlassStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateKlassStrategy {
  
  @Override
  public IKlassModel execute(IListModel<IKlassModel> listModel) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", listModel);
    execute(GET_OR_CREATE_KLASS, requestMap);
    return null;
  }
}
