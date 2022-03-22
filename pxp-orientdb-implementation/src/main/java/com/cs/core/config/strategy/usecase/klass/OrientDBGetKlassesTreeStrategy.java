package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.CategoryTreeInformationModel;
import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getKlassesTreeStrategy")
public class OrientDBGetKlassesTreeStrategy extends OrientDBBaseStrategy
    implements IGetKlassesTreeStrategy {
  
  @Override
  public ICategoryTreeInformationModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(GET_KLASSES_TREE, requestMap, CategoryTreeInformationModel.class);
  }
}
