package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.GetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetGovernanceRulesByKlassAndTaxonomyIdsStrategy extends OrientDBBaseStrategy
    implements IGetGovernanceRulesByKlassAndTaxonomyIdsStrategy {
  
  @Override
  public IListModel<IGetKeyPerformanceIndexModel> execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GOVERNANCE_RULES_BY_KLASS_AND_TAXONOMY_IDS, model,
        new TypeReference<ListModel<GetKeyPerformanceIndexModel>>()
        {
          
        });
  }
}
