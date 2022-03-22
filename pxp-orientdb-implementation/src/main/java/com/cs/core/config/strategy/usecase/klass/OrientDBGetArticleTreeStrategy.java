package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getArticleTreeStrategy")
public class OrientDBGetArticleTreeStrategy extends OrientDBBaseStrategy
    implements IGetKlassTreeStrategy {
  
  @Override
  public IConfigEntityTreeInformationModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_ARTICLE_TREE, requestMap, ConfigEntityTreeInformationModel.class);
  }
}
