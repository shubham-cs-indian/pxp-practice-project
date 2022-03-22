package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetTextAssetsByIdsStrategy extends OrientDBBaseStrategy
    implements IGetTextAssetsByIdsStrategy {
  
  public static final String useCase = "GetTextAssetsByIds";
  
  @Override
  public IListModel<IKlass> execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(useCase, requestMap, new TypeReference<ListModel<IKlass>>()
    {
      
    });
  }
}
