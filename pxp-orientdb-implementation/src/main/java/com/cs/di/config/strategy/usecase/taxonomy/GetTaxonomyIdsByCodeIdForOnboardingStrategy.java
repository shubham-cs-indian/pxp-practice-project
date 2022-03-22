package com.cs.di.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetTaxonomyIdsByCodeIdForOnboardingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("getTaxonomyIdsByCodeIdForOnboardingStrategy")
public class GetTaxonomyIdsByCodeIdForOnboardingStrategy extends OrientDBBaseStrategy implements IGetTaxonomyIdsByCodeIdForOnboardingStrategy {
  
  @SuppressWarnings("unchecked")
  @Override
  public IIdsListParameterModel execute(IListModel<String> model) throws Exception
  {
    Map<String, List<String>> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, (List<String>) model.getList());
    return execute(GET_TAXONOMY_IDS_BY_CODE_ID_FOR_ONBOARDING, requestMap, IdsListParameterModel.class);
  }
}
