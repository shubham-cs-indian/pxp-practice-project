package com.cs.core.config.strategy.usecase.klass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetTaxonomyHierarchyForSelectedTaxonomyStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class GetTaxonomyHierarchyForSelectedTaxonomyStrategy extends OrientDBBaseStrategy
    implements IGetTaxonomyHierarchyForSelectedTaxonomyStrategy {
  
  public static final String useCase = "GetTaxonomyHierarchyForSelectedTaxonomy";
  
  @Override
  @SuppressWarnings("unchecked")
  public IGetConfigDetailsForCustomTabModel execute(IConfigDetailsForSwitchTypeRequestModel model)
      throws Exception
  {
    Map<String, Object> requestMap = ObjectMapperUtil.convertValue(model, HashMap.class);
    return execute(useCase, requestMap, GetConfigDetailsForCustomTabModel.class);
  }
}
